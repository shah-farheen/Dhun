package com.bits.farheen.dhun;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.RemoteViews;

import com.bits.farheen.dhun.events.PauseMusic;
import com.bits.farheen.dhun.events.PlayStatusChange;
import com.bits.farheen.dhun.events.PositionChange;
import com.bits.farheen.dhun.events.QueueChange;
import com.bits.farheen.dhun.models.SongsModel;
import com.bits.farheen.dhun.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by farheen on 11/18/16
 * Service which will play music in background. Needs the following in the intent:
 * 1) {@link ArrayList<SongsModel>} songQueue with key Constants.CURRENT_MUSIC_QUEUE
 * 2) int positionToPlay with key Constants.POSITION_TO_PLAY
 * 3) int playBackType with key Constants.PLAYBACK_TYPE
 */

public class PlayMusicService extends Service implements MediaPlayer.OnCompletionListener,
        MediaPlayer.OnErrorListener, AudioManager.OnAudioFocusChangeListener {

    private Gson gson;
    private MediaPlayer mediaPlayer;
    private SharedPreferences dataFile;
    private ServiceHandler mServiceHandler;
    private static final String TAG = "PlayMusicService";
    private Type songListType = new TypeToken<ArrayList<SongsModel>>(){}.getType();

    private boolean isAlreadyRunning;
    private int currentPlayingPosition;
    private ArrayList<SongsModel> currentQueue;

    private class ServiceHandler extends Handler{
        ServiceHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

        }
    }

    @Override
    public void onCreate() {
        gson = new Gson();
        isAlreadyRunning = false;
        initMediaPlayer();

        HandlerThread thread = new HandlerThread("PlayMusicThread", Process.THREAD_PRIORITY_AUDIO);
        thread.start();
        mServiceHandler = new ServiceHandler(thread.getLooper());

        dataFile = getSharedPreferences(Constants.DATA_FILE, MODE_PRIVATE);
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ArrayList<SongsModel> songQueue =
                gson.fromJson(intent.getStringExtra(Constants.CURRENT_MUSIC_QUEUE), songListType);
        int positionToPlay = intent.getIntExtra(Constants.POSITION_TO_PLAY, 0);

        if(isAlreadyRunning){
            switch (intent.getIntExtra(Constants.PLAYBACK_TYPE, Constants.PLAYBACK_CHANGE_PLAYLIST)){
                case Constants.PLAYBACK_RESUME :
                    playMusic();
                    break;
                case Constants.PLAYBACK_CHANGE_POSITION :
                    changePosition(positionToPlay);
                    break;
                case Constants.PLAYBACK_CHANGE_PLAYLIST :
                    prePlayingSetup(songQueue, positionToPlay);
                    break;
            }
        }
        else {
            prePlayingSetup(songQueue, positionToPlay);
        }

        isAlreadyRunning = true;
        return START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        terminateMediaPlayer();
        EventBus.getDefault().unregister(this);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(Constants.FOREGROUND_SERVICE_NOTIFICATION_ID);
        super.onDestroy();
    }

    private void initMediaPlayer(){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
    }

    private void terminateMediaPlayer(){
        mediaPlayer.stop();
        mediaPlayer.release();
        mediaPlayer = null;
    }

    private void prePlayingSetup(ArrayList<SongsModel> songQueue, int positionToPlay){
        currentQueue = songQueue;
        currentPlayingPosition = positionToPlay;
        startPlayingMusic(currentQueue.get(currentPlayingPosition).getDataUri());
        EventBus.getDefault().post(new QueueChange(currentPlayingPosition, currentQueue));
        dataFile.edit().putInt(Constants.LAST_PLAYED_POSITION, positionToPlay).apply();
        dataFile.edit().putString(Constants.CURRENT_MUSIC_QUEUE, gson.toJson(currentQueue, songListType)).apply();
    }

    private void startPlayingMusic(final String songPath){
        mServiceHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(songPath);
                    mediaPlayer.prepare();
                    playMusic();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void playMusic(){
        mServiceHandler.post(new Runnable() {
            @Override
            public void run() {
                mediaPlayer.start();
                startForeground(Constants.FOREGROUND_SERVICE_NOTIFICATION_ID, getNotification());
                dataFile.edit().putBoolean(Constants.IS_MUSIC_PLAYING, true).apply();
                EventBus.getDefault().post(new PlayStatusChange(true));
            }
        });
    }

    @Subscribe
    public void pauseMusic(PauseMusic pauseMusic){
        mediaPlayer.pause();
        stopForeground(false);
        dataFile.edit().putBoolean(Constants.IS_MUSIC_PLAYING, false).apply();
        EventBus.getDefault().post(new PlayStatusChange(false));
    }

    public void changePosition(int newPosition){
        currentPlayingPosition = newPosition;
        startPlayingMusic(currentQueue.get(newPosition).getDataUri());
        EventBus.getDefault().post(new PositionChange(newPosition));
        dataFile.edit().putInt(Constants.LAST_PLAYED_POSITION, newPosition).apply();
    }

    private Notification getNotification(){
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.now_playing_notification);
        notificationLayout.setTextViewText(R.id.text_song_name, currentQueue.get(currentPlayingPosition).getTitle());

        Intent musicIntent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent musicPendingIntent =
                PendingIntent.getActivity(this, Constants.NOW_PLAYING_NOTIFICATION_INTENT_REQUEST_CODE,
                        musicIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.play)
                .setContentIntent(musicPendingIntent)
                .setContentTitle(currentQueue.get(currentPlayingPosition).getTitle())
                .setContentText(currentQueue.get(currentPlayingPosition).getArtist())
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.music))
                .setStyle(new android.support.v7.app.NotificationCompat.MediaStyle()
                        );
//                .setContent(notificationLayout);
        return builder.build();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(currentPlayingPosition == currentQueue.size()-1){
            dataFile.edit().putBoolean(Constants.IS_MUSIC_PLAYING, false).apply();
            EventBus.getDefault().post(new PlayStatusChange(false));
            stopSelf();
        }
        else {
            currentPlayingPosition++;
            startPlayingMusic(currentQueue.get(currentPlayingPosition).getDataUri());
            EventBus.getDefault().post(new PositionChange(currentPlayingPosition));
            dataFile.edit().putInt(Constants.LAST_PLAYED_POSITION, currentPlayingPosition).apply();
        }
    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }

    @Override
    public void onAudioFocusChange(int focusChange) {
        switch (focusChange){
            case AudioManager.AUDIOFOCUS_GAIN :
                if(mediaPlayer == null){
                    initMediaPlayer();
                } else if(!mediaPlayer.isPlaying()){
                    mediaPlayer.start();
                } else {
                    mediaPlayer.setVolume(1.0f, 1.0f);
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS :
                terminateMediaPlayer();
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT :
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.pause();
                }
                break;

            case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK :
                if(mediaPlayer.isPlaying()){
                    mediaPlayer.setVolume(0.1f, 0.1f);
                }
                break;

            default :
                break;
        }
    }

}

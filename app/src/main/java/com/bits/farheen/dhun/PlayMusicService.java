package com.bits.farheen.dhun;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.PowerManager;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.bits.farheen.dhun.events.PauseMusic;
import com.bits.farheen.dhun.events.PlayMusic;
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
 */

public class PlayMusicService extends Service implements MediaPlayer.OnCompletionListener{

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
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setWakeMode(this, PowerManager.PARTIAL_WAKE_LOCK);
        mediaPlayer.setOnCompletionListener(this);

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
                    playMusic(new PlayMusic());
                    break;
                case Constants.PLAYBACK_CHANGE_POSITION :
                    changePosition(positionToPlay);
                    break;
                case Constants.PLAYBACK_CHANGE_PLAYLIST :
                    currentQueue = songQueue;
                    currentPlayingPosition = positionToPlay;
                    startPlayingMusic(currentQueue.get(currentPlayingPosition).getDataUri());
                    EventBus.getDefault().post(new QueueChange(currentPlayingPosition, currentQueue));
                    dataFile.edit().putString(Constants.CURRENT_MUSIC_QUEUE, gson.toJson(currentQueue, songListType)).apply();
                    break;
            }
        }
        else {
            currentQueue = songQueue;
            currentPlayingPosition = positionToPlay;
            startPlayingMusic(currentQueue.get(currentPlayingPosition).getDataUri());
            EventBus.getDefault().post(new QueueChange(currentPlayingPosition, currentQueue));
            dataFile.edit().putString(Constants.CURRENT_MUSIC_QUEUE, gson.toJson(currentQueue, songListType)).apply();
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
        mediaPlayer.stop();
        mediaPlayer.release();
        EventBus.getDefault().unregister(this);

        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(Constants.FOREGROUND_SERVICE_NOTIFICATION_ID);
        super.onDestroy();
    }

    private Notification getNotification(){
        RemoteViews notificationLayout = new RemoteViews(getPackageName(), R.layout.now_playing_notification);
        notificationLayout.setTextViewText(R.id.text_song_name, currentQueue.get(currentPlayingPosition).getTitle());
        notificationLayout.setImageViewUri(R.id.image_song_thumb, Uri.parse(currentQueue.get(currentPlayingPosition).getSongThumb()));

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.play)
                .setContent(notificationLayout);
        return builder.build();
    }

    private void startPlayingMusic(final String songPath){
        mServiceHandler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(songPath);
                    mediaPlayer.prepare();
                    playMusic(new PlayMusic());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Subscribe
    public void playMusic(PlayMusic playMusic){
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

    public void changePosition(int position){
        startPlayingMusic(currentQueue.get(position).getDataUri());
        EventBus.getDefault().post(new PositionChange(position));
        dataFile.edit().putInt(Constants.LAST_PLAYED_POSITION, position).apply();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        if(currentPlayingPosition == currentQueue.size()-1){
            dataFile.edit().putBoolean(Constants.IS_MUSIC_PLAYING, false).apply();
            stopSelf();
        }
        else {
            currentPlayingPosition++;
            startPlayingMusic(currentQueue.get(currentPlayingPosition).getDataUri());
            EventBus.getDefault().post(new PositionChange(currentPlayingPosition));
            dataFile.edit().putInt(Constants.LAST_PLAYED_POSITION, currentPlayingPosition).apply();
        }
    }

}

package com.bits.farheen.dhun;

import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.bits.farheen.dhun.events.PauseMusic;
import com.bits.farheen.dhun.events.PlayMusic;
import com.bits.farheen.dhun.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;

/**
 * Created by farheen on 11/18/16
 */

public class PlayMusicService extends Service implements MediaPlayer.OnCompletionListener{

    private MediaPlayer mediaPlayer;
    private ServiceHandler mServiceHandler;
    private SharedPreferences dataFile;
    private static final String TAG = "PlayMusicService";

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
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mediaPlayer.setOnCompletionListener(this);

        HandlerThread thread = new HandlerThread("PlayMusicThread", Process.THREAD_PRIORITY_AUDIO);
        thread.start();
        mServiceHandler = new ServiceHandler(thread.getLooper());

        dataFile = getSharedPreferences(Constants.DATA_FILE, MODE_PRIVATE);
        EventBus.getDefault().register(this);
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        mServiceHandler.post(new Runnable() {
            @Override
            public void run() {
                startPlayingMusic(intent.getStringExtra(Constants.SONG_PATH));
            }
        });
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
        super.onDestroy();
    }

    private Notification getNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.play)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        return builder.build();
    }

    private void startPlayingMusic(String songPath){
        Uri songUri = Uri.parse(songPath);
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(this, songUri);
            mediaPlayer.prepare();
            playMusic(new PlayMusic());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void playMusic(PlayMusic playMusic){
        mediaPlayer.start();
        startForeground(Constants.FOREGROUND_SERVICE_NOTIFICATION_ID, getNotification());
        dataFile.edit().putBoolean(Constants.IS_MUSIC_PLAYING, true).apply();
    }

    @Subscribe
    public void pauseMusic(PauseMusic pauseMusic){
        mediaPlayer.pause();
        stopForeground(false);
        dataFile.edit().putBoolean(Constants.IS_MUSIC_PLAYING, false).apply();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        dataFile.edit().putBoolean(Constants.IS_MUSIC_PLAYING, false).apply();
        stopSelf();
    }

}

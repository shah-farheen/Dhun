package com.bits.farheen.dhun;

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
import android.os.Process;
import android.support.annotation.Nullable;
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
        HandlerThread thread = new HandlerThread("PlayMusicThread", Process.THREAD_PRIORITY_AUDIO);
        thread.start();
        mServiceHandler = new ServiceHandler(thread.getLooper());
        dataFile = getSharedPreferences(Constants.DATA_FILE, MODE_PRIVATE);
        EventBus.getDefault().register(this);
        mediaPlayer.setOnCompletionListener(this);
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

    public void startPlayingMusic(String songPath){
        Uri songUri = Uri.parse(songPath);
        try {
            mediaPlayer.setDataSource(this, songUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
            dataFile.edit().putBoolean(Constants.IS_MUSIC_PLAYING, true).apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void playMusic(PlayMusic playMusic){
        mediaPlayer.start();
        dataFile.edit().putBoolean(Constants.IS_MUSIC_PLAYING, true).apply();
    }

    @Subscribe
    public void pauseMusic(PauseMusic pauseMusic){
        mediaPlayer.pause();
        dataFile.edit().putBoolean(Constants.IS_MUSIC_PLAYING, false).apply();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        dataFile.edit().putBoolean(Constants.IS_MUSIC_PLAYING, false).apply();
        stopSelf();
    }

}

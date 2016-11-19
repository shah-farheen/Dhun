package com.bits.farheen.dhun;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.IOException;

/**
 * Created by farheen on 11/18/16
 */

public class PlayMusicService extends Service{

    private static final String TAG = "PlayMusicService";

    private MediaPlayer mediaPlayer;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void playMusic(String songPath){
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        Uri songUri = Uri.parse(songPath);
        try {
            mediaPlayer.setLooping(true);
            mediaPlayer.setDataSource(this, songUri);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void pauseMusic(){
        mediaPlayer.pause();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}

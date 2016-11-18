package com.bits.farheen.dhun;

import android.app.IntentService;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.bits.farheen.dhun.utils.Constants;

import java.io.IOException;

/**
 * Created by farheen on 11/18/16
 */

public class PlayMusicService extends IntentService{

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * "PlayMusicService" name Used to name the worker thread, important only for debugging.
     */
    public PlayMusicService() {
        super("PlayMusicService");
    }

    private MediaPlayer mediaPlayer;

    @Override
    protected void onHandleIntent(Intent intent) {
        mediaPlayer = new MediaPlayer();
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        playMusic(intent.getStringExtra(Constants.SONG_PATH));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    public void playMusic(String songPath){
        Uri songUri = Uri.parse(songPath);
        try {
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

}

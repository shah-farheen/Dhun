package com.bits.farheen.dhun.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;

import com.bits.farheen.dhun.events.PauseMusic;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by farheen on 12/3/16
 */

public class MusicIntentReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if(intent.getAction().equals(AudioManager.ACTION_AUDIO_BECOMING_NOISY)){
            EventBus.getDefault().post(new PauseMusic());
        }
    }
}

package com.bits.farheen.dhun.events;

import com.bits.farheen.dhun.models.SongsModel;

import java.util.ArrayList;

/**
 * Created by farheen on 11/29/16
 */

public class MusicQueue {
    private ArrayList<SongsModel> queue;

    public MusicQueue(ArrayList<SongsModel> queue) {
        this.queue = queue;
    }

    public ArrayList<SongsModel> getQueue() {
        return queue;
    }
}

package com.bits.farheen.dhun.events;

import com.bits.farheen.dhun.models.SongsModel;

import java.util.ArrayList;

/**
 * Created by farheen on 11/29/16
 */

public class QueueChange {
    private int positionToPlay;
    private ArrayList<SongsModel> queue;

    public QueueChange(int positionToPlay, ArrayList<SongsModel> queue) {
        this.queue = queue;
        this.positionToPlay = positionToPlay;
    }

    public int getPositionToPlay() {
        return positionToPlay;
    }

    public ArrayList<SongsModel> getQueue() {
        return queue;
    }
}

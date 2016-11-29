package com.bits.farheen.dhun.events;

/**
 * Created by farheen on 11/29/16
 */

public class PlayStatusChange {
    private boolean isPlaying;

    public PlayStatusChange(boolean isPlaying) {
        this.isPlaying = isPlaying;
    }

    public boolean isPlaying() {
        return isPlaying;
    }
}

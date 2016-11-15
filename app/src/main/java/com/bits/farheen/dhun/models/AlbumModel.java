package com.bits.farheen.dhun.models;

/**
 * Created by farheen on 11/16/16
 */

public class AlbumModel {
    private String albumArt;
    private String key;
    private String name;
    private String artist;
    private int numSongs;

    public AlbumModel() {

    }

    public String getAlbumArt() {
        return albumArt;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }

    public String getArtist() {
        return artist;
    }

    public int getNumSongs() {
        return numSongs;
    }

    public void setAlbumArt(String albumArt) {
        this.albumArt = albumArt;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setNumSongs(int numSongs) {
        this.numSongs = numSongs;
    }

    @Override
    public String toString() {
        return "AlbumModel{" +
                "name='" + name + '\'' +
                ", artist='" + artist + '\'' +
                ", numSongs=" + numSongs +
                ", albumArt='" + albumArt + '\'' +
                '}';
    }
}

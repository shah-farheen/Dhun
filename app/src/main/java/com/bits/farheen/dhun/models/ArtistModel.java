package com.bits.farheen.dhun.models;

/**
 * Created by farheen on 11/15/16
 */

public class ArtistModel {
    private String key;
    private String artist;
    private String numAlbums;
    private String numTracks;

    public ArtistModel() {

    }

    public String getKey() {
        return key;
    }

    public String getArtist() {
        return artist;
    }

    public String getNumAlbums() {
        return numAlbums;
    }

    public String getNumTracks() {
        return numTracks;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setNumAlbums(String numAlbums) {
        this.numAlbums = numAlbums;
    }

    public void setNumTracks(String numTracks) {
        this.numTracks = numTracks;
    }

    @Override
    public String toString() {
        return "ArtistModel{" +
                "artist='" + artist + '\'' +
                ", numAlbums='" + numAlbums + '\'' +
                ", numTracks='" + numTracks + '\'' +
                '}';
    }
}

package com.bits.farheen.dhun.models;

/**
 * Created by farheen on 11/11/16
 */

public class SongsModel {
    private long songId;
    private String title;
    private String artist;
    private String album;
    private int albumId;
    private long duration;
    private String dataUri;
    private String songThumb;

    public SongsModel() {

    }

    public String getSongThumb() {
        return songThumb;
    }

    public int getAlbumId() {
        return albumId;
    }

    public String getDataUri() {
        return dataUri;
    }

    public long getSongId() {
        return songId;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getAlbum() {
        return album;
    }

    public long getDuration() {
        return duration;
    }

    public void setSongId(long songId) {
        this.songId = songId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public void setDataUri(String dataUri) {
        this.dataUri = dataUri;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }

    public void setSongThumb(String songThumb) {
        this.songThumb = songThumb;
    }

    @Override
    public String toString() {
        return "SongsModel{" +
                "songId=" + songId +
                ", title='" + title + '\'' +
                ", artist='" + artist + '\'' +
                ", album='" + album + '\'' +
                ", duration=" + duration +
                ", dataUri='" + dataUri + '\'' +
                '}';
    }
}

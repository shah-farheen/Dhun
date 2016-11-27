package com.bits.farheen.dhun.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;

import com.bits.farheen.dhun.models.AlbumModel;
import com.bits.farheen.dhun.models.ArtistModel;
import com.bits.farheen.dhun.models.SongsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by farheen on 11/27/16
 */

public class Utility {

    public static List<SongsModel> querySongs(Context context, String selection, String[] selectionArgs){
        List<SongsModel> songsList = new ArrayList<>();

        String[] projectionColumns = {MediaStore.Audio.Media._ID,
                                      MediaStore.Audio.Media.TITLE,
                                      MediaStore.Audio.Media.ARTIST,
                                      MediaStore.Audio.Media.ALBUM,
                                      MediaStore.Audio.Media.DURATION,
                                      MediaStore.Audio.Media.DATA};

        Cursor songsCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projectionColumns,
                selection == null ? null : selection + "=?",
                selectionArgs,
                MediaStore.Audio.Media.DEFAULT_SORT_ORDER);

        if(songsCursor != null){
            while (songsCursor.moveToNext()){
                SongsModel songsModel = new SongsModel();
                songsModel.setSongId(songsCursor.getLong(songsCursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                songsModel.setTitle(songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                songsModel.setArtist(songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                songsModel.setAlbum(songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                songsModel.setDuration(songsCursor.getLong(songsCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                songsModel.setDataUri(songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                songsList.add(songsModel);
            }
            songsCursor.close();
        }
        return songsList;
    }

    public static List<AlbumModel> queryAlbums(Context context, String selection, String[] selectionArgs){
        List<AlbumModel> albumList = new ArrayList<>();

        String[] projectionColumns = {MediaStore.Audio.Albums.ALBUM_ART,
                                      MediaStore.Audio.Albums.ALBUM_KEY,
                                      MediaStore.Audio.Albums.ALBUM,
                                      MediaStore.Audio.Albums.ARTIST,
                                      MediaStore.Audio.Albums.NUMBER_OF_SONGS};

        Cursor albumsCursor = context.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projectionColumns,
                selection == null ? null : selection + "=?",
                selectionArgs,
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);

        if(albumsCursor != null){
            while (albumsCursor.moveToNext()){
                AlbumModel albumModel = new AlbumModel();
                albumModel.setAlbumArt(albumsCursor.getString(albumsCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART)));
                albumModel.setKey(albumsCursor.getString(albumsCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_KEY)));
                albumModel.setName(albumsCursor.getString(albumsCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM)));
                albumModel.setArtist(albumsCursor.getString(albumsCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST)));
                albumModel.setNumSongs(albumsCursor.getInt(albumsCursor.getColumnIndex(MediaStore.Audio.Albums.NUMBER_OF_SONGS)));
                albumList.add(albumModel);
            }
            albumsCursor.close();
        }
        return albumList;
    }

    public static List<ArtistModel> queryArtists(Context context, String selection, String[] selectionArgs){
        List<ArtistModel> artistsList = new ArrayList<>();

        String[] projectionColumns = {MediaStore.Audio.Artists.ARTIST_KEY,
                                      MediaStore.Audio.Artists.ARTIST,
                                      MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                                      MediaStore.Audio.Artists.NUMBER_OF_TRACKS};

        Cursor artistsCursor = context.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                projectionColumns,
                selection == null ? null : selection + "=?",
                selectionArgs,
                MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);

        if(artistsCursor != null){
            while (artistsCursor.moveToNext()){
                ArtistModel artistModel = new ArtistModel();
                artistModel.setKey(artistsCursor.getString(artistsCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST_KEY)));
                artistModel.setArtist(artistsCursor.getString(artistsCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
                artistModel.setNumAlbums(artistsCursor.getString(artistsCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)));
                artistModel.setNumTracks(artistsCursor.getString(artistsCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)));
                artistsList.add(artistModel);
            }
            artistsCursor.close();
        }
        return artistsList;
    }

}

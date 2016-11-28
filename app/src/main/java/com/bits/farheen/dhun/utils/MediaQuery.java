package com.bits.farheen.dhun.utils;

import android.content.Context;
import android.database.Cursor;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.provider.MediaStore;

import com.bits.farheen.dhun.models.AlbumModel;
import com.bits.farheen.dhun.models.ArtistModel;
import com.bits.farheen.dhun.models.SongsModel;

import java.util.ArrayList;

/**
 * Created by farheen on 11/27/16
 */

public class MediaQuery {

    private Context mContext;
    private Handler uiHandler;
    private Handler mWorkerHandler;
    private QueryCompletionListener queryCompletionListener;

    public MediaQuery(Context context, String threadName,
                      QueryCompletionListener queryCompletionListener, Handler uiHandler){
        mContext = context;
        this.uiHandler = uiHandler;
        this.queryCompletionListener = queryCompletionListener;
        HandlerThread workerThread = new HandlerThread(threadName, Process.THREAD_PRIORITY_URGENT_DISPLAY);
        workerThread.start();
        mWorkerHandler = new Handler(workerThread.getLooper());
    }

    public interface QueryCompletionListener{
        void songQueryCompleted(ArrayList<SongsModel> songsList);
        void albumQueryCompleted(ArrayList<AlbumModel> albumList);
        void artistQueryCompleted(ArrayList<ArtistModel> artistList);
        void songThumbQueryCompleted(long songId, String songThumb);
    }

    public void querySongs(final String selection, final String[] selectionArgs){
        mWorkerHandler.post(new Runnable() {
            @Override
            public void run() {
                final ArrayList<SongsModel> songsList = querySongsSync(selection, selectionArgs);
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        queryCompletionListener.songQueryCompleted(songsList);
                    }
                });
            }
        });
    }

    public void queryAlbums(final String selection, final String[] selectionArgs){
        mWorkerHandler.post(new Runnable() {
            @Override
            public void run() {
                final ArrayList<AlbumModel> albumList = queryAlbumsSync(selection, selectionArgs);
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        queryCompletionListener.albumQueryCompleted(albumList);
                    }
                });
            }
        });
    }

    public void queryArtists(final String selection, final String[] selectionArgs){
        mWorkerHandler.post(new Runnable() {
            @Override
            public void run() {
                final ArrayList<ArtistModel> artistList = queryArtistsSync(selection, selectionArgs);
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        queryCompletionListener.artistQueryCompleted(artistList);
                    }
                });
            }
        });
    }

    public void querySongThumb(final long songId, final int albumId){
        mWorkerHandler.post(new Runnable() {
            @Override
            public void run() {
                final String songThumb = queryAlbumThumbSync(albumId);
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        queryCompletionListener.songThumbQueryCompleted(songId, songThumb);
                    }
                });
            }
        });
    }

    private ArrayList<SongsModel> querySongsSync(String selection, String[] selectionArgs){
        ArrayList<SongsModel> songsList = new ArrayList<>();

        String[] projectionColumns = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA};

        Cursor songsCursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
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
                songsModel.setAlbumId(songsCursor.getInt(songsCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
                songsModel.setDuration(songsCursor.getLong(songsCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                songsModel.setDataUri(songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                songsModel.setSongThumb(null);
                songsList.add(songsModel);
            }
            songsCursor.close();
        }
        return songsList;
    }

    private ArrayList<AlbumModel> queryAlbumsSync(String selection, String[] selectionArgs){
        ArrayList<AlbumModel> albumList = new ArrayList<>();

        String[] projectionColumns = {MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.ALBUM_KEY,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS};

        Cursor albumsCursor = mContext.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
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

    private ArrayList<ArtistModel> queryArtistsSync(String selection, String[] selectionArgs){
        ArrayList<ArtistModel> artistsList = new ArrayList<>();

        String[] projectionColumns = {MediaStore.Audio.Artists.ARTIST_KEY,
                MediaStore.Audio.Artists.ARTIST,
                MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                MediaStore.Audio.Artists.NUMBER_OF_TRACKS};

        Cursor artistsCursor = mContext.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
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

    private String queryAlbumThumbSync(int albumId){
        String albumThumb = null;

        String[] projectionColumns = {MediaStore.Audio.Albums.ALBUM_ART};

        Cursor thumbCursor = mContext.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projectionColumns,
                MediaStore.Audio.Albums._ID + "=?",
                new String[]{String.valueOf(albumId)},
                MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);

        if(thumbCursor != null){
            if(thumbCursor.moveToFirst()){
                albumThumb = thumbCursor.getString(thumbCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM_ART));
            }
            thumbCursor.close();
        }
        return albumThumb;
    }

}

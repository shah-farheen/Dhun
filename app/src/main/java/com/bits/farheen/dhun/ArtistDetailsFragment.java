package com.bits.farheen.dhun;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bits.farheen.dhun.adapters.AlbumListAdapter;
import com.bits.farheen.dhun.adapters.SongsListAdapter;
import com.bits.farheen.dhun.models.AlbumModel;
import com.bits.farheen.dhun.models.SongsModel;
import com.bits.farheen.dhun.utils.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistDetailsFragment extends Fragment {

    private Context mContext;
    private Bundle dataBundle;
    private static final String TAG = "ArtistDetailsFragment";

    public ArtistDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBundle = getArguments();
    }

    @BindView(R.id.recycler_albums) RecyclerView recyclerAlbums;
    @BindView(R.id.recycler_songs) RecyclerView recyclerSongs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artist_details, container, false);
        ButterKnife.bind(this, view);

        recyclerSongs.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerSongs.setAdapter(new SongsListAdapter(querySongs(), mContext));

        recyclerAlbums.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        recyclerAlbums.setAdapter(new AlbumListAdapter(queryAlbums(), mContext));

        return view;
    }

    public List<SongsModel> querySongs(){
        List<SongsModel> songsList = new ArrayList<>();

        String[] projectionColumns = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA};

        Cursor songsCursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projectionColumns,
                MediaStore.Audio.Media.ARTIST_KEY + "=?",
                new String[]{dataBundle.getString(Constants.ARTIST_KEY)},
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

    public List<AlbumModel> queryAlbums(){
        List<AlbumModel> albumList = new ArrayList<>();
        String[] projectionColumns = {MediaStore.Audio.Albums.ALBUM_ART,
                MediaStore.Audio.Albums.ALBUM_KEY,
                MediaStore.Audio.Albums.ALBUM,
                MediaStore.Audio.Albums.ARTIST,
                MediaStore.Audio.Albums.NUMBER_OF_SONGS};

        Cursor albumsCursor = mContext.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projectionColumns,
                MediaStore.Audio.Albums.ARTIST + "=?",
                new String[]{dataBundle.getString(Constants.ARTIST_NAME)},
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

}

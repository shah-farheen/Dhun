package com.bits.farheen.dhun;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bits.farheen.dhun.adapters.AlbumListAdapter;
import com.bits.farheen.dhun.models.AlbumModel;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumsFragment extends Fragment {

    private Context mContext;
    private static final String TAG = "AlbumsFragment";

    public AlbumsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @BindView(R.id.recycler_albums) RecyclerView recyclerAlbums;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        ButterKnife.bind(this, view);

        AlbumListAdapter albumListAdapter = new AlbumListAdapter(queryAlbums(), mContext);
        recyclerAlbums.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerAlbums.setAdapter(albumListAdapter);

        return view;
    }

    public List<AlbumModel> queryAlbums(){
        List<AlbumModel> albumList = new ArrayList<>();
        String[] projectionColumns = {MediaStore.Audio.Albums.ALBUM_ART,
                                      MediaStore.Audio.Albums.ALBUM_KEY,
                                      MediaStore.Audio.Albums.ALBUM,
                                      MediaStore.Audio.Albums.ARTIST,
                                      MediaStore.Audio.Albums.NUMBER_OF_SONGS};

        Cursor albumsCursor = mContext.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                projectionColumns, null, null, MediaStore.Audio.Albums.DEFAULT_SORT_ORDER);

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
            Log.e(TAG, "queryAlbums: " + albumList.size());
        }
        return albumList;
    }

}

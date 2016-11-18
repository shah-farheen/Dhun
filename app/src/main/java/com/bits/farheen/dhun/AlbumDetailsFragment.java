package com.bits.farheen.dhun;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bits.farheen.dhun.adapters.SongsListAdapter;
import com.bits.farheen.dhun.models.SongsModel;
import com.bits.farheen.dhun.utils.Constants;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumDetailsFragment extends Fragment {

    private Context mContext;
    private Bundle dataBundle;
    private static final String TAG = "AlbumDetailsFragment";

    public AlbumDetailsFragment() {
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

    @BindView(R.id.image_album_thumb) ImageView imageAlbumThumb;
    @BindView(R.id.text_album_name) TextView textAlbumName;
    @BindView(R.id.text_num_songs) TextView textNumSongs;
    @BindView(R.id.recycler_songs) RecyclerView recyclerSongs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_album_details, container, false);
        ButterKnife.bind(this, view);

        Glide.with(mContext).load(dataBundle.getString(Constants.ALBUM_ART))
                .placeholder(R.drawable.music)
                .into(imageAlbumThumb);
        textAlbumName.setText(dataBundle.getString(Constants.ALBUM_NAME));
        String stringNumSongs = dataBundle.getInt(Constants.NUM_SONGS) + " songs";
        textNumSongs.setText(stringNumSongs);

        SongsListAdapter songsListAdapter = new SongsListAdapter(querySongs(), mContext);
        recyclerSongs.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerSongs.setAdapter(songsListAdapter);

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
                MediaStore.Audio.Media.ALBUM_KEY + "=?",
                new String[]{dataBundle.getString(Constants.ALBUM_KEY)},
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
            Log.e(TAG, "querySongs: " + songsList.size());
        }
        return songsList;
    }

}

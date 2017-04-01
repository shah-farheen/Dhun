package com.bits.farheen.dhun.music.albums;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bits.farheen.dhun.R;
import com.bits.farheen.dhun.music.songs.SongsListAdapter;
import com.bits.farheen.dhun.models.AlbumModel;
import com.bits.farheen.dhun.models.ArtistModel;
import com.bits.farheen.dhun.models.SongsModel;
import com.bits.farheen.dhun.utils.Constants;
import com.bits.farheen.dhun.utils.MediaQuery;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AlbumDetailsFragment extends Fragment implements MediaQuery.QueryCompletionListener{

    private Context mContext;
    private Bundle dataBundle;
    private SongsListAdapter songsListAdapter;
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
        MediaQuery mediaQuery = new MediaQuery(mContext, getClass().getName(), this, new Handler());
        songsListAdapter = new SongsListAdapter(new ArrayList<SongsModel>(), mContext);
        mediaQuery.querySongs(MediaStore.Audio.Media.ALBUM_KEY,
                new String[]{dataBundle.getString(Constants.ALBUM_KEY)});
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

        recyclerSongs.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerSongs.setAdapter(songsListAdapter);

        return view;
    }

    @Override
    public void songQueryCompleted(ArrayList<SongsModel> songsList) {
        songsListAdapter.addData(songsList);
    }

    @Override
    public void albumQueryCompleted(ArrayList<AlbumModel> albumList) {

    }

    @Override
    public void artistQueryCompleted(ArrayList<ArtistModel> artistList) {

    }

    @Override
    public void songThumbQueryCompleted(long songId, String songThumb) {

    }
}

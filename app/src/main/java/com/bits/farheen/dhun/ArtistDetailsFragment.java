package com.bits.farheen.dhun;


import android.content.Context;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bits.farheen.dhun.adapters.AlbumListAdapter;
import com.bits.farheen.dhun.adapters.SongsListAdapter;
import com.bits.farheen.dhun.utils.Constants;
import com.bits.farheen.dhun.utils.Utility;

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
        recyclerSongs.setAdapter(new SongsListAdapter(Utility.querySongs(mContext,
                MediaStore.Audio.Media.ARTIST_KEY,
                new String[]{dataBundle.getString(Constants.ARTIST_KEY)}), mContext));

        recyclerAlbums.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        recyclerAlbums.setAdapter(new AlbumListAdapter(Utility.queryAlbums(mContext,
                MediaStore.Audio.Albums.ARTIST,
                new String[]{dataBundle.getString(Constants.ARTIST_NAME)}), mContext));

        return view;
    }

}

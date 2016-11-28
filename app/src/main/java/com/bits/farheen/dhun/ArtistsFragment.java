package com.bits.farheen.dhun;


import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bits.farheen.dhun.adapters.ArtistsListAdapter;
import com.bits.farheen.dhun.models.AlbumModel;
import com.bits.farheen.dhun.models.ArtistModel;
import com.bits.farheen.dhun.models.SongsModel;
import com.bits.farheen.dhun.utils.MediaQuery;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistsFragment extends Fragment implements MediaQuery.QueryCompletionListener{

    private Context mContext;
    private ArtistsListAdapter artistsListAdapter;
    private static final String TAG = "ArtistsFragment";

    public ArtistsFragment() {
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
        MediaQuery mediaQuery = new MediaQuery(mContext, getClass().getName(), this, new Handler());
        artistsListAdapter = new ArtistsListAdapter(new ArrayList<ArtistModel>(), mContext);
        mediaQuery.queryArtists(null, null);
    }

    @BindView(R.id.recycler_artists) RecyclerView recyclerArtists;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        ButterKnife.bind(this, view);

        recyclerArtists.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerArtists.setAdapter(artistsListAdapter);

        return view;
    }

    @Override
    public void songQueryCompleted(ArrayList<SongsModel> songsList) {

    }

    @Override
    public void albumQueryCompleted(ArrayList<AlbumModel> albumList) {

    }

    @Override
    public void artistQueryCompleted(ArrayList<ArtistModel> artistList) {
        artistsListAdapter.addData(artistList);
    }

    @Override
    public void songThumbQueryCompleted(long songId, String songThumb) {

    }
}

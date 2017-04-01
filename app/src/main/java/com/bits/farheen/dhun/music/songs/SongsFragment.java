package com.bits.farheen.dhun.music.songs;


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

import com.bits.farheen.dhun.R;
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
public class SongsFragment extends Fragment implements MediaQuery.QueryCompletionListener{

    private Context mContext;
    private MediaQuery mediaQuery;
    private SongsListAdapter songsListAdapter;
    private static final String TAG = "SongsFragment";

    public SongsFragment() {
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
        mediaQuery = new MediaQuery(mContext, getClass().getName(), this, new Handler());
        songsListAdapter = new SongsListAdapter(new ArrayList<SongsModel>(), mContext);
        mediaQuery.querySongs(null, null);
    }

    @BindView(R.id.recycler_songs) RecyclerView recyclerSongs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_songs, container, false);
        ButterKnife.bind(this, view);

        recyclerSongs.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerSongs.setAdapter(songsListAdapter);

        return view;
    }

    @Override
    public void songQueryCompleted(ArrayList<SongsModel> songsList) {
        songsListAdapter.addData(songsList);
        for(SongsModel song : songsList){
            mediaQuery.querySongThumb(song.getSongId(), song.getAlbumId());
        }
    }

    @Override
    public void albumQueryCompleted(ArrayList<AlbumModel> albumList) {

    }

    @Override
    public void artistQueryCompleted(ArrayList<ArtistModel> artistList) {

    }

    @Override
    public void songThumbQueryCompleted(long songId, String songThumb) {
        songsListAdapter.updateSongInfo(songId, songThumb);
    }
}

package com.bits.farheen.dhun;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bits.farheen.dhun.adapters.AlbumListAdapter;
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
public class AlbumsFragment extends Fragment implements MediaQuery.QueryCompletionListener{

    private Context mContext;
    private AlbumListAdapter albumListAdapter;
    private static final String TAG = "AlbumsFragment";

    public AlbumsFragment() {
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
        albumListAdapter = new AlbumListAdapter(new ArrayList<AlbumModel>(), mContext);
        mediaQuery.queryAlbums(null, null);
    }

    @BindView(R.id.recycler_albums) RecyclerView recyclerAlbums;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_albums, container, false);
        ButterKnife.bind(this, view);

        recyclerAlbums.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerAlbums.setAdapter(albumListAdapter);

        return view;
    }

    @Override
    public void songQueryCompleted(ArrayList<SongsModel> songsList) {

    }

    @Override
    public void albumQueryCompleted(ArrayList<AlbumModel> albumList) {
        albumListAdapter.addData(albumList);
    }

    @Override
    public void artistQueryCompleted(ArrayList<ArtistModel> artistList) {

    }

    @Override
    public void songThumbQueryCompleted(long songId, String songThumb) {

    }
}

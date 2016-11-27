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
import com.bits.farheen.dhun.utils.Utility;

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

        AlbumListAdapter albumListAdapter = new AlbumListAdapter(Utility.queryAlbums(mContext, null, null), mContext);
        recyclerAlbums.setLayoutManager(new GridLayoutManager(mContext, 2));
        recyclerAlbums.setAdapter(albumListAdapter);

        return view;
    }

}

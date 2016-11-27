package com.bits.farheen.dhun;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bits.farheen.dhun.adapters.ArtistsListAdapter;
import com.bits.farheen.dhun.models.ArtistModel;
import com.bits.farheen.dhun.utils.Utility;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ArtistsFragment extends Fragment {

    private Context mContext;
    private static final String TAG = "ArtistsFragment";

    public ArtistsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @BindView(R.id.recycler_artists) RecyclerView recyclerArtists;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_artists, container, false);
        ButterKnife.bind(this, view);

        ArtistsListAdapter adapter = new ArtistsListAdapter(Utility.queryArtists(mContext, null, null), mContext);
        recyclerArtists.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerArtists.setAdapter(adapter);

        return view;
    }

}

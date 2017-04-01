package com.bits.farheen.dhun.misc;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bits.farheen.dhun.R;

import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlaylistsFragment extends Fragment {


    public PlaylistsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_playlists, container, false);
        ButterKnife.bind(this, view);

        return view;
    }

}

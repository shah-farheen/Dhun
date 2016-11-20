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

        ArtistsListAdapter adapter = new ArtistsListAdapter(queryArtists(), mContext);
        recyclerArtists.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerArtists.setAdapter(adapter);

        return view;
    }

    public List<ArtistModel> queryArtists(){
        List<ArtistModel> artistsList = new ArrayList<>();
        String[] projectionColumns = {MediaStore.Audio.Artists.ARTIST_KEY,
                                      MediaStore.Audio.Artists.ARTIST,
                                      MediaStore.Audio.Artists.NUMBER_OF_ALBUMS,
                                      MediaStore.Audio.Artists.NUMBER_OF_TRACKS};

        Cursor artistsCursor = mContext.getContentResolver().query(MediaStore.Audio.Artists.EXTERNAL_CONTENT_URI,
                projectionColumns, null, null, MediaStore.Audio.Artists.DEFAULT_SORT_ORDER);

        if(artistsCursor != null){
            while (artistsCursor.moveToNext()){
                ArtistModel artistModel = new ArtistModel();
                artistModel.setKey(artistsCursor.getString(artistsCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST_KEY)));
                artistModel.setArtist(artistsCursor.getString(artistsCursor.getColumnIndex(MediaStore.Audio.Artists.ARTIST)));
                artistModel.setNumAlbums(artistsCursor.getString(artistsCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_ALBUMS)));
                artistModel.setNumTracks(artistsCursor.getString(artistsCursor.getColumnIndex(MediaStore.Audio.Artists.NUMBER_OF_TRACKS)));
                artistsList.add(artistModel);
            }
            artistsCursor.close();
        }
        return artistsList;
    }
}

package com.bits.farheen.dhun;


import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bits.farheen.dhun.models.SongsModel;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class SongsFragment extends Fragment {

    private Context mContext;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_songs, container, false);
    }

    public void querySongs(){

        List<SongsModel> songsList = new ArrayList<>();

        String[] projectionColumns = {MediaStore.Audio.Media._ID,
                                      MediaStore.Audio.Media.TITLE,
                                      MediaStore.Audio.Media.ARTIST,
                                      MediaStore.Audio.Media.ALBUM,
                                      MediaStore.Audio.Media.DURATION};

        Cursor songsCursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projectionColumns, null, null, MediaStore.Audio.Media.TITLE);

        if(songsCursor != null){
            while (songsCursor.moveToNext()){
                SongsModel songsModel = new SongsModel();
                songsModel.setSongId(songsCursor.getLong(songsCursor.getColumnIndex(MediaStore.Audio.Media._ID)));
                songsModel.setTitle(songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
                songsModel.setArtist(songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
                songsModel.setAlbum(songsCursor.getString(songsCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM)));
                songsModel.setDuration(songsCursor.getLong(songsCursor.getColumnIndex(MediaStore.Audio.Media.DURATION)));
                songsList.add(songsModel);
                Log.d(TAG, "querySongs: " + songsModel.toString());
            }
            songsCursor.close();
        }

    }

}

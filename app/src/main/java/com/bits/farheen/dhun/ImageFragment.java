package com.bits.farheen.dhun;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bits.farheen.dhun.utils.Constants;
import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class ImageFragment extends Fragment {

    private Context mContext;

    public ImageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @BindView(R.id.image_song_thumb) ImageView imageSongThumb;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);
        ButterKnife.bind(this, view);
        Bundle dataBundle = getArguments();
        Glide.with(mContext).load(dataBundle.getString(Constants.SONG_THUMB_PATH))
                .fitCenter()
                .placeholder(R.drawable.music)
                .into(imageSongThumb);

        return view;
    }

}

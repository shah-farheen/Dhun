package com.bits.farheen.dhun.adapters;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.bits.farheen.dhun.ImageFragment;
import com.bits.farheen.dhun.models.SongsModel;
import com.bits.farheen.dhun.utils.Constants;

import java.util.ArrayList;

/**
 * Created by farheen on 11/27/16
 */

public class NowPlayingThumbAdapter extends FragmentStatePagerAdapter{

    private Bundle dataBundle;
    private ArrayList<SongsModel> nowPlayingList;

    public NowPlayingThumbAdapter(FragmentManager fm, ArrayList<SongsModel> nowPlayingList) {
        super(fm);
        this.nowPlayingList = nowPlayingList;
        dataBundle = new Bundle();
    }

    @Override
    public Fragment getItem(int position) {
        dataBundle.putString(Constants.SONG_THUMB_PATH, nowPlayingList.get(position).getSongThumb());
        ImageFragment imageFragment = new ImageFragment();
        imageFragment.setArguments(dataBundle);
        return imageFragment;
    }

    @Override
    public int getCount() {
        return nowPlayingList.size();
    }

    public void addData(ArrayList<SongsModel> data){
        nowPlayingList.addAll(data);
        notifyDataSetChanged();
    }
}

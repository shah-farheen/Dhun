package com.bits.farheen.dhun.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bits.farheen.dhun.AlbumsFragment;
import com.bits.farheen.dhun.ArtistsFragment;
import com.bits.farheen.dhun.PlaylistsFragment;
import com.bits.farheen.dhun.SongsFragment;

/**
 * Created by farheen on 11/18/16
 */

public class ViewPagerAdapter extends FragmentPagerAdapter{

    private String[] pagerTitles = {"Artists", "Albums", "Songs", "PlayLists"};

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0 :
                return new ArtistsFragment();
            case 1 :
                return new AlbumsFragment();
            case 2 :
                return new SongsFragment();
            case 3 :
                return new PlaylistsFragment();
            default :
                return null;
        }
    }

    @Override
    public int getCount() {
        return pagerTitles.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return pagerTitles[position];
    }
}

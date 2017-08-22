package com.bits.farheen.dhun;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bits.farheen.dhun.misc.PlaylistsFragment;
import com.bits.farheen.dhun.music.albums.AlbumsFragment;
import com.bits.farheen.dhun.music.artists.ArtistsFragment;
import com.bits.farheen.dhun.music.songs.SongsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class TabsFragment extends Fragment {

    private Context mContext;
    private static final String TAG = "TabsFragment";

    public TabsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @BindView(R.id.tab_layout) TabLayout tabLayout;
    @BindView(R.id.view_pager) ViewPager viewPager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tabs, container, false);
        ButterKnife.bind(this, view);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getActivity().getSupportFragmentManager());
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return view;
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private String[] pagerTitles = {"Artists", "Albums", "Songs", "PlayLists"};

        ViewPagerAdapter(FragmentManager fm) {
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

}

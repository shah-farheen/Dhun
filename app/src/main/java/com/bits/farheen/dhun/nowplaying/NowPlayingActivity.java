package com.bits.farheen.dhun.nowplaying;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bits.farheen.dhun.PlayMusicService;
import com.bits.farheen.dhun.R;
import com.bits.farheen.dhun.events.PauseMusic;
import com.bits.farheen.dhun.events.PlayStatusChange;
import com.bits.farheen.dhun.events.PositionChange;
import com.bits.farheen.dhun.events.QueueChange;
import com.bits.farheen.dhun.models.SongsModel;
import com.bits.farheen.dhun.music.songs.SongsListAdapter;
import com.bits.farheen.dhun.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.lang.reflect.Type;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NowPlayingActivity extends AppCompatActivity {

    @BindView(R.id.image_show_queue) ImageView imageShowQueue;
    @BindView(R.id.image_play_pause) ImageView imagePlayPause;
    @BindView(R.id.image_previous) ImageView imagePrevious;
    @BindView(R.id.image_next) ImageView imageNext;
    @BindView(R.id.view_pager_song_thumbs) ViewPager viewPagerSongThumbs;
    @BindView(R.id.recycler_song_queue) RecyclerView recyclerSongQueue;

    private Gson gson;
    private Context mContext;
    private SharedPreferences dataFile;
    private int currentPlayingPosition;
    private SongsListAdapter songsListAdapter;
    private ArrayList<SongsModel> currentQueue;
    private NowPlayingThumbAdapter nowPlayingThumbAdapter;
    private Type songListType = new TypeToken<ArrayList<SongsModel>>(){}.getType();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        ButterKnife.bind(this);
        gson = new Gson();
        mContext = this;
        dataFile = getSharedPreferences(Constants.DATA_FILE, MODE_PRIVATE);

        nowPlayingThumbAdapter =
                new NowPlayingThumbAdapter(getSupportFragmentManager(), new ArrayList<SongsModel>());
        viewPagerSongThumbs.setAdapter(nowPlayingThumbAdapter);

        songsListAdapter = new SongsListAdapter(new ArrayList<SongsModel>(), this);
        recyclerSongQueue.setLayoutManager(new LinearLayoutManager(this));
        recyclerSongQueue.setAdapter(songsListAdapter);
        recyclerSongQueue.setNestedScrollingEnabled(false);

        initListeners();
    }

    void initListeners(){
        imagePlayPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // If music is playing then pause it
                if(dataFile.getBoolean(Constants.IS_MUSIC_PLAYING, false)){
                    EventBus.getDefault().post(new PauseMusic());
                    imagePlayPause.setImageResource(R.drawable.play);
                }
                else {
                    Intent playMusicIntent = new Intent(mContext, PlayMusicService.class)
                            .putExtra(Constants.CURRENT_MUSIC_QUEUE, gson.toJson(currentQueue, songListType))
                            .putExtra(Constants.POSITION_TO_PLAY, currentPlayingPosition)
                            .putExtra(Constants.PLAYBACK_TYPE, Constants.PLAYBACK_RESUME);
                    startService(playMusicIntent);
                    imagePlayPause.setImageResource(R.drawable.pause);
                }
            }
        });

        imageShowQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerSongQueue.setY(0.0f);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        currentQueue = gson.fromJson(dataFile.getString(Constants.CURRENT_MUSIC_QUEUE, null), songListType);
        currentPlayingPosition = dataFile.getInt(Constants.LAST_PLAYED_POSITION, 0);
        if(currentQueue != null){
            nowPlayingThumbAdapter.addData(currentQueue);
            songsListAdapter.addData(currentQueue);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }

    @Subscribe
    public void onQueueChange(QueueChange queueChange){
        currentQueue = queueChange.getQueue();
        currentPlayingPosition = queueChange.getPositionToPlay();
        nowPlayingThumbAdapter.replaceData(currentQueue);
        songsListAdapter.replaceData(currentQueue);
        viewPagerSongThumbs.setCurrentItem(currentPlayingPosition, false);
    }

    @Subscribe
    public void onPositionChange(PositionChange positionChange){
        currentPlayingPosition = positionChange.getPosition();
        viewPagerSongThumbs.setCurrentItem(currentPlayingPosition, false);
    }

    @Subscribe
    public void onPlayStatusChange(PlayStatusChange playStatusChange){
        if(playStatusChange.isPlaying()){
            imagePlayPause.setImageResource(R.drawable.pause);
        }
        else {
            imagePlayPause.setImageResource(R.drawable.play);
        }
    }

    private class NowPlayingThumbAdapter extends FragmentStatePagerAdapter {

        private Bundle dataBundle;
        private ArrayList<SongsModel> nowPlayingList;

        NowPlayingThumbAdapter(FragmentManager fm, ArrayList<SongsModel> nowPlayingList) {
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

        void addData(ArrayList<SongsModel> data){
            nowPlayingList.addAll(data);
            notifyDataSetChanged();
        }

        void replaceData(ArrayList<SongsModel> data){
            nowPlayingList.clear();
            nowPlayingList.addAll(data);
            notifyDataSetChanged();
        }
    }
}

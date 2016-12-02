package com.bits.farheen.dhun;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bits.farheen.dhun.adapters.NowPlayingThumbAdapter;
import com.bits.farheen.dhun.adapters.SongsListAdapter;
import com.bits.farheen.dhun.events.PlayStatusChange;
import com.bits.farheen.dhun.events.PositionChange;
import com.bits.farheen.dhun.events.QueueChange;
import com.bits.farheen.dhun.models.SongsModel;
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

    private Gson gson;
    private SharedPreferences dataFile;
    private int currentlyPlayingPosition;
    private SongsListAdapter songsListAdapter;
    private ArrayList<SongsModel> currentQueue;
    private NowPlayingThumbAdapter nowPlayingThumbAdapter;
    private Type songListType = new TypeToken<ArrayList<SongsModel>>(){}.getType();

    @BindView(R.id.view_pager_song_thumbs) ViewPager viewPagerSongThumbs;
    @BindView(R.id.image_show_queue) ImageView imageShowQueue;
    @BindView(R.id.recycler_song_queue) RecyclerView recyclerSongQueue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_playing);
        ButterKnife.bind(this);
        gson = new Gson();
        dataFile = getSharedPreferences(Constants.DATA_FILE, MODE_PRIVATE);

        nowPlayingThumbAdapter =
                new NowPlayingThumbAdapter(getSupportFragmentManager(), new ArrayList<SongsModel>());
        viewPagerSongThumbs.setAdapter(nowPlayingThumbAdapter);

        songsListAdapter = new SongsListAdapter(new ArrayList<SongsModel>(), this);
        recyclerSongQueue.setLayoutManager(new LinearLayoutManager(this));
        recyclerSongQueue.setAdapter(songsListAdapter);

        initListeners();
    }

    void initListeners(){
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
        currentlyPlayingPosition = dataFile.getInt(Constants.LAST_PLAYED_POSITION, 0);
        if(currentQueue != null){
            nowPlayingThumbAdapter.addData(currentQueue);
            songsListAdapter.addData(currentQueue);
        }
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        dataFile.edit().putString(Constants.CURRENT_MUSIC_QUEUE, gson.toJson(currentQueue, songListType)).apply();
        super.onStop();
    }

    @Subscribe
    public void onQueueChange(QueueChange queueChange){
        currentQueue = queueChange.getQueue();
        currentlyPlayingPosition = queueChange.getPositionToPlay();
    }

    @Subscribe
    public void onPositionChange(PositionChange positionChange){
        currentlyPlayingPosition = positionChange.getPosition();
    }

    @Subscribe
    public void onPlayStatusChange(PlayStatusChange playStatusChange){

    }
}

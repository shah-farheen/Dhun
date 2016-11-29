package com.bits.farheen.dhun;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bits.farheen.dhun.events.MusicQueue;
import com.bits.farheen.dhun.events.PauseMusic;
import com.bits.farheen.dhun.models.AlbumModel;
import com.bits.farheen.dhun.models.ArtistModel;
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

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar_main_activity) Toolbar toolbar;
    @BindView(R.id.text_song_name) TextView textSongName;
    @BindView(R.id.text_song_artist) TextView textSongArtist;
    @BindView(R.id.image_play_pause) ImageView imagePlayPause;
    @BindView(R.id.frag_container) FrameLayout fragContainer;
    @BindView(R.id.bottom_view) View bottomView;

    private Gson gson;
    private Context mContext;
    private SharedPreferences dataFile;
    private FragmentManager fragmentManager;
    private static final String TAG = "MainActivity";
    private int lastPlayedPosition;
    private ArrayList<SongsModel> currentQueue;
    private Type songListType = new TypeToken<ArrayList<SongsModel>>(){}.getType();

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;
        gson = new Gson();
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();
        dataFile = getSharedPreferences(Constants.DATA_FILE, MODE_PRIVATE);

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.READ_EXTERNAL_STORAGE_REQUEST_CODE);
        }
        else {
            initViews();
        }

        initListeners();
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        dataFile.edit().putString(Constants.CURRENT_MUSIC_QUEUE, gson.toJson(currentQueue, songListType)).apply();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == Constants.READ_EXTERNAL_STORAGE_REQUEST_CODE){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                initViews();
            }
            else {
                Toast.makeText(getApplicationContext(), "Permission Needed", Toast.LENGTH_SHORT).show();
            }
        }
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
                            .putExtra(Constants.CURRENT_MUSIC_QUEUE, dataFile.getString(Constants.CURRENT_MUSIC_QUEUE, null))
                            .putExtra(Constants.POSITION_TO_PLAY, dataFile.getInt(Constants.LAST_PLAYED_POSITION, 0));
                    startService(playMusicIntent);
                    imagePlayPause.setImageResource(R.drawable.pause);
                }
            }
        });
    }

    void initViews(){
        currentQueue = gson.fromJson(dataFile.getString(Constants.CURRENT_MUSIC_QUEUE, null), songListType);
        lastPlayedPosition = dataFile.getInt(Constants.LAST_PLAYED_POSITION, 0);
        if(currentQueue == null){
            bottomView.setVisibility(View.GONE);
        }
        else {
            textSongName.setText(currentQueue.get(lastPlayedPosition).getTitle());
            textSongArtist.setText(currentQueue.get(lastPlayedPosition).getArtist());
            if(dataFile.getBoolean(Constants.IS_MUSIC_PLAYING, false)){
                imagePlayPause.setImageResource(R.drawable.pause);
            }
            else {
                imagePlayPause.setImageResource(R.drawable.play);
            }
        }
    }

    @Subscribe
    public void getUpdatedQueue(MusicQueue musicQueue){
        currentQueue = musicQueue.getQueue();
    }

    @Subscribe
    public void openAlbumDetails(AlbumModel album){
        Bundle albumBundle = new Bundle();
        albumBundle.putString(Constants.ALBUM_KEY, album.getKey());
        albumBundle.putInt(Constants.NUM_SONGS, album.getNumSongs());
        albumBundle.putString(Constants.ALBUM_NAME, album.getName());
        albumBundle.putString(Constants.ALBUM_ART, album.getAlbumArt());

        AlbumDetailsFragment albumDetailsFragment = new AlbumDetailsFragment();
        albumDetailsFragment.setArguments(albumBundle);

        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
                .add(R.id.frag_container, albumDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

    @Subscribe
    public void openArtistDetails(ArtistModel artist){
        Bundle artistBundle = new Bundle();
        artistBundle.putString(Constants.ARTIST_KEY, artist.getKey());
        artistBundle.putString(Constants.ARTIST_NAME, artist.getArtist());

        ArtistDetailsFragment artistDetailsFragment = new ArtistDetailsFragment();
        artistDetailsFragment.setArguments(artistBundle);

        fragmentManager.beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .add(R.id.frag_container, artistDetailsFragment)
                .addToBackStack(null)
                .commit();
    }

}

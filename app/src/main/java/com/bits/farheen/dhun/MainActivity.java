package com.bits.farheen.dhun;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bits.farheen.dhun.models.AlbumModel;
import com.bits.farheen.dhun.utils.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.text_song_name) TextView textSongName;
    @BindView(R.id.text_song_artist) TextView textSongArtist;
    @BindView(R.id.image_play_pause) ImageView imagePlayPause;
    @BindView(R.id.frag_container) FrameLayout fragContainer;

    private FragmentManager fragmentManager;

    @SuppressLint("InlinedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        fragmentManager = getSupportFragmentManager();

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    Constants.READ_EXTERNAL_STORAGE_REQUEST_CODE);
        }
        else {
            initViews();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
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

    void initViews(){
        fragmentManager.beginTransaction()
                .add(R.id.frag_container, new TabsFragment())
                .commit();
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

}

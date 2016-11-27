package com.bits.farheen.dhun.adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bits.farheen.dhun.PlayMusicService;
import com.bits.farheen.dhun.R;
import com.bits.farheen.dhun.models.SongsModel;
import com.bits.farheen.dhun.utils.Constants;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by farheen on 11/13/16
 */

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.SongViewHolder>{

    private Gson gson;
    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<SongsModel> songsList;
    private SharedPreferences dataFile;
    private Type songListType = new TypeToken<ArrayList<SongsModel>>(){}.getType();

    public SongsListAdapter(ArrayList<SongsModel> songsList, Context context){
        mContext = context;
        gson = new Gson();
        inflater = LayoutInflater.from(mContext);
        this.songsList = songsList;
        dataFile = mContext.getSharedPreferences(Constants.DATA_FILE, Context.MODE_PRIVATE);
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.single_song, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        SongsModel currentSong = songsList.get(position);
        holder.textSongTitle.setText(currentSong.getTitle());
        holder.textSongArtist.setText(currentSong.getArtist());
        holder.textSongAlbum.setText(currentSong.getAlbum());

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataFile.edit().putString(Constants.CURRENT_MUSIC_QUEUE, gson.toJson(songsList, songListType)).apply();
                Intent playMusicIntent = new Intent(mContext, PlayMusicService.class);
                playMusicIntent.putExtra(Constants.CURRENT_MUSIC_QUEUE, gson.toJson(songsList, songListType));
                mContext.startService(playMusicIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    class SongViewHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.image_song_thumb) ImageView imageSongThumb;
        @BindView(R.id.text_song_title) TextView textSongTitle;
        @BindView(R.id.text_song_artist) TextView textSongArtist;
        @BindView(R.id.text_song_album) TextView textSongAlbum;
        @BindView(R.id.root_view) View rootView;

        SongViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

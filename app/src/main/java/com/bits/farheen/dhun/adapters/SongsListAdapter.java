package com.bits.farheen.dhun.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bits.farheen.dhun.PlayMusicService;
import com.bits.farheen.dhun.R;
import com.bits.farheen.dhun.events.MusicQueue;
import com.bits.farheen.dhun.models.SongsModel;
import com.bits.farheen.dhun.utils.Constants;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;

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
    private static final String TAG = "SongsListAdapter";
    private Type songListType = new TypeToken<ArrayList<SongsModel>>(){}.getType();

    public SongsListAdapter(ArrayList<SongsModel> songsList, Context context){
        mContext = context;
        gson = new Gson();
        inflater = LayoutInflater.from(mContext);
        this.songsList = songsList;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.single_song, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SongViewHolder holder, int position) {
        SongsModel currentSong = songsList.get(position);
        holder.textSongTitle.setText(currentSong.getTitle());
        holder.textSongArtist.setText(currentSong.getArtist());
        holder.textSongAlbum.setText(currentSong.getAlbum());

        Glide.with(mContext).load(currentSong.getSongThumb())
                .fitCenter()
                .placeholder(R.drawable.music)
                .into(holder.imageSongThumb);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MusicQueue(songsList));
                Intent playMusicIntent = new Intent(mContext, PlayMusicService.class)
                        .putExtra(Constants.CURRENT_MUSIC_QUEUE, gson.toJson(songsList, songListType))
                        .putExtra(Constants.POSITION_TO_PLAY, holder.getAdapterPosition())
                        .putExtra(Constants.PLAYBACK_TYPE, Constants.PLAYBACK_CHANGE_PLAYLIST);
                mContext.startService(playMusicIntent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public void addData(ArrayList<SongsModel> data){
        songsList.addAll(data);
        notifyDataSetChanged();
    }

    public void updateSongInfo(long songId, String songThumb){
        if(songThumb != null){
            for(int i=0; i<songsList.size(); i++){
                SongsModel songsModel = songsList.get(i);
                if(songsModel.getSongId() == songId){
                    songsModel.setSongThumb(songThumb);
                    notifyItemChanged(i, songsModel);
                    break;
                }
            }
        }
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

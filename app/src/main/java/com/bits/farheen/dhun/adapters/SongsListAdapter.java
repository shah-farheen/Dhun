package com.bits.farheen.dhun.adapters;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bits.farheen.dhun.R;
import com.bits.farheen.dhun.models.SongsModel;

import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by farheen on 11/13/16
 */

public class SongsListAdapter extends RecyclerView.Adapter<SongsListAdapter.SongViewHolder>{

    private Context mContext;
    private LayoutInflater inflater;
    private List<SongsModel> songsList;

    public SongsListAdapter(List<SongsModel> songsList, Context context){
        mContext = context;
        inflater = LayoutInflater.from(mContext);
        this.songsList = songsList;
    }

    @Override
    public SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.single_song, parent, false);
        return new SongViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SongViewHolder holder, int position) {
        final SongsModel currentSong = songsList.get(position);
        holder.textSongTitle.setText(currentSong.getTitle());
        holder.textSongArtist.setText(currentSong.getArtist());
        holder.textSongAlbum.setText(currentSong.getAlbum());

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri songUri = Uri.parse(currentSong.getDataUri());
                MediaPlayer mediaPlayer = new MediaPlayer();
                mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
                try {
                    mediaPlayer.setDataSource(mContext, songUri);
                    mediaPlayer.prepare();
                    mediaPlayer.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
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

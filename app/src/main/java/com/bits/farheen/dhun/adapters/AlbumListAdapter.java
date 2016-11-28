package com.bits.farheen.dhun.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bits.farheen.dhun.R;
import com.bits.farheen.dhun.models.AlbumModel;
import com.bumptech.glide.Glide;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by farheen on 11/16/16
 */

public class AlbumListAdapter extends RecyclerView.Adapter<AlbumListAdapter.AlbumHolder>{

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<AlbumModel> albumList;

    public AlbumListAdapter(ArrayList<AlbumModel> albumList, Context context){
        this.albumList = albumList;
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public AlbumHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.single_album, parent, false);
        return new AlbumHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlbumHolder holder, int position) {
        final AlbumModel currentAlbum = albumList.get(position);
        holder.textAlbumName.setText(currentAlbum.getName());
        holder.textAlbumArtist.setText(currentAlbum.getArtist());
        String stringNumSongs = currentAlbum.getNumSongs() + " songs";
        holder.textNumSongs.setText(stringNumSongs);

        Glide.with(mContext).load(currentAlbum.getAlbumArt())
                .fitCenter()
                .placeholder(R.drawable.music)
                .into(holder.imageAlbumThumb);

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(currentAlbum);
            }
        });
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }

    public void addData(ArrayList<AlbumModel> data){
        albumList.addAll(data);
        notifyDataSetChanged();
    }

    class AlbumHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.image_album_thumb) ImageView imageAlbumThumb;
        @BindView(R.id.text_album_name) TextView textAlbumName;
        @BindView(R.id.text_album_artist) TextView textAlbumArtist;
        @BindView(R.id.text_num_songs) TextView textNumSongs;
        @BindView(R.id.root_view) View rootView;

        AlbumHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

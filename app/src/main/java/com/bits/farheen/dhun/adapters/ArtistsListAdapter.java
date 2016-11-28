package com.bits.farheen.dhun.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bits.farheen.dhun.R;
import com.bits.farheen.dhun.models.ArtistModel;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by farheen on 11/16/16
 */

public class ArtistsListAdapter extends RecyclerView.Adapter<ArtistsListAdapter.ArtistHolder>{

    private Context mContext;
    private LayoutInflater inflater;
    private ArrayList<ArtistModel> artistList;

    public ArtistsListAdapter(ArrayList<ArtistModel> artistList, Context context){
        this.artistList = artistList;
        mContext = context;
        inflater = LayoutInflater.from(mContext);
    }

    @Override
    public ArtistHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.single_artist, parent, false);
        return new ArtistHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ArtistHolder holder, int position) {
        final ArtistModel currentArtist = artistList.get(position);
        holder.textArtistName.setText(currentArtist.getArtist());
        holder.textNumAlbums.setText(currentArtist.getNumAlbums());
        holder.textNumTracks.setText(currentArtist.getNumTracks());

        holder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(currentArtist);
            }
        });
    }

    @Override
    public int getItemCount() {
        return artistList.size();
    }

    public void addData(ArrayList<ArtistModel> data){
        artistList.addAll(data);
        notifyDataSetChanged();
    }

    class ArtistHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.image_artist_thumb) ImageView imageArtistThumb;
        @BindView(R.id.text_artist_name) TextView textArtistName;
        @BindView(R.id.text_num_albums) TextView textNumAlbums;
        @BindView(R.id.text_num_tracks) TextView textNumTracks;
        @BindView(R.id.root_view) View rootView;

        ArtistHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}

package com.bits.farheen.dhun.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by farheen on 11/27/16
 */

public class NowPlayingThumbAdapter extends RecyclerView.Adapter<NowPlayingThumbAdapter.NowPlayingThumbHolder>{

    public NowPlayingThumbAdapter(){

    }

    @Override
    public NowPlayingThumbHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(NowPlayingThumbHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class NowPlayingThumbHolder extends RecyclerView.ViewHolder{

        public NowPlayingThumbHolder(View itemView) {
            super(itemView);
        }
    }
}

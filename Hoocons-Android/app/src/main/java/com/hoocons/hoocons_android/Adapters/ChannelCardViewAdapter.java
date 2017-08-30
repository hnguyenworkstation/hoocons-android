package com.hoocons.hoocons_android.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.ChannelCardViewHolder;

/**
 * Created by hungnguyen on 8/30/17.
 */

public class ChannelCardViewAdapter extends RecyclerView.Adapter<ChannelCardViewHolder> {
    public ChannelCardViewAdapter() {
    }

    @Override
    public ChannelCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_cardview_viewholder, parent, false);
        return new ChannelCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelCardViewHolder holder, int position) {
        holder.init();
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public void onViewRecycled(ChannelCardViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }
}

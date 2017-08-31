package com.hoocons.hoocons_android.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.EventCardViewHolder;

/**
 * Created by hungnguyen on 8/30/17.
 */

public class EventCardViewAdapter extends RecyclerView.Adapter<EventCardViewHolder> {
    public EventCardViewAdapter() {
    }

    @Override
    public EventCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_cardview_viewholder, parent, false);
        return new EventCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventCardViewHolder holder, int position) {
        holder.initView(position);
    }

    @Override
    public int getItemCount() {
        return 30;
    }

    @Override
    public void onViewRecycled(EventCardViewHolder holder) {
        holder.onViewRecycled();
        super.onViewRecycled(holder);
    }
}

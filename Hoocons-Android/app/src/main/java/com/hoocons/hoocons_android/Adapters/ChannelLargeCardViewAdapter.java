package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Interface.OnChannelProfileClickListener;
import com.hoocons.hoocons_android.Networking.Responses.ChannelProfileResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.ChannelLargeCardViewHolder;

import java.util.List;

/**
 * Created by hungnguyen on 8/30/17.
 */

public class ChannelLargeCardViewAdapter extends RecyclerView.Adapter<ChannelLargeCardViewHolder> {
    private Context context;
    private List<ChannelProfileResponse> channelProfileResponseList;
    private OnChannelProfileClickListener listener;

    public ChannelLargeCardViewAdapter(Context context, List<ChannelProfileResponse> channelProfileResponseList,
                                       OnChannelProfileClickListener listener) {
        this.context = context;
        this.channelProfileResponseList = channelProfileResponseList;
        this.listener = listener;
    }

    @Override
    public ChannelLargeCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_large_cardview_viewholder,
                parent, false);
        return new ChannelLargeCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ChannelLargeCardViewHolder holder, int position) {
        holder.init(context, channelProfileResponseList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return channelProfileResponseList.size();
    }

    @Override
    public void onViewRecycled(ChannelLargeCardViewHolder holder) {
        super.onViewRecycled(holder);
        holder.onViewRecycled();
    }
}

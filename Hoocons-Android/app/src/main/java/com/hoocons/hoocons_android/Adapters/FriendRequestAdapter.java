package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Interface.OnFriendRequestAdapterListener;
import com.hoocons.hoocons_android.Networking.ApiViewSets.FriendshipRequestApiViewSet;
import com.hoocons.hoocons_android.Networking.Responses.FriendshipRequestResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.FriendConnectionViewHolder;
import com.hoocons.hoocons_android.ViewHolders.FriendRequestViewHolder;

import java.util.List;

/**
 * Created by hungnguyen on 8/26/17.
 */

public class FriendRequestAdapter extends RecyclerView.Adapter<FriendRequestViewHolder> {
    private Context context;
    private List<FriendshipRequestResponse> friendshipRequestResponseList;
    private OnFriendRequestAdapterListener listener;

    public FriendRequestAdapter(Context context, List<FriendshipRequestResponse>
            friendshipRequestResponseList, OnFriendRequestAdapterListener listener) {
        this.context = context;
        this.friendshipRequestResponseList = friendshipRequestResponseList;
        this.listener = listener;
    }

    @Override
    public FriendRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_request_viewholder, parent, false);
        return new FriendRequestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendRequestViewHolder holder, int position) {
        holder.initHolder(context, friendshipRequestResponseList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return friendshipRequestResponseList.size();
    }

    @Override
    public void onViewRecycled(FriendRequestViewHolder holder) {
        holder.onViewRecycled();
        super.onViewRecycled(holder);
    }
}

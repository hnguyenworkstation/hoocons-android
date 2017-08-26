package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Interface.OnFriendConnectionAdapterListener;
import com.hoocons.hoocons_android.Networking.Responses.RelationshipResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.FriendConnectionViewHolder;

import java.util.List;

/**
 * Created by hungnguyen on 8/26/17.
 */

public class FriendConnectionsAdapter extends RecyclerView.Adapter<FriendConnectionViewHolder> {
    private Context context;
    private List<RelationshipResponse> relationshipResponseList;
    private OnFriendConnectionAdapterListener listener;

    public FriendConnectionsAdapter(Context context, List<RelationshipResponse> relationshipResponseList,
                                    OnFriendConnectionAdapterListener listener) {
        this.context = context;
        this.relationshipResponseList = relationshipResponseList;
        this.listener = listener;
    }

    @Override
    public FriendConnectionViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.friend_connection_viewholder, parent, false);
        return new FriendConnectionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FriendConnectionViewHolder holder, int position) {
        holder.initHolder(relationshipResponseList.get(position), listener, position);
    }

    @Override
    public int getItemCount() {
        return relationshipResponseList.size();
    }

    @Override
    public void onViewRecycled(FriendConnectionViewHolder holder) {
        holder.onViewRecycled();
        super.onViewRecycled(holder);
    }
}

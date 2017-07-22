package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Interface.CommentAdapterListener;
import com.hoocons.hoocons_android.Networking.Responses.CommentResponse;
import com.hoocons.hoocons_android.ViewHolders.CommentViewHolder;

import java.util.List;

/**
 * Created by hungnguyen on 7/21/17.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    private Context context;
    private List<CommentResponse> commentResponseList;
    private CommentAdapterListener listener;

    public CommentsAdapter(final Context context, List<CommentResponse> commentResponses,
                           CommentAdapterListener listener) {
        this.context = context;
        this.commentResponseList = commentResponses;
        this.listener = listener;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        holder.initViewHolder(commentResponseList.get(position), context, position, listener);
    }

    @Override
    public int getItemCount() {
        return commentResponseList.size();
    }
}

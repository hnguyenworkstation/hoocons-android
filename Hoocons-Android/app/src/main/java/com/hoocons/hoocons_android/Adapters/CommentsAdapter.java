package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Helpers.AppConstant;
import com.hoocons.hoocons_android.Interface.CommentAdapterListener;
import com.hoocons.hoocons_android.Networking.Responses.CommentResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.CommentViewHolder;

import java.util.List;

/**
 * Created by hungnguyen on 7/21/17.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentViewHolder> {
    private Context context;
    private List<CommentResponse> commentResponseList;
    private CommentAdapterListener listener;
    private final int COMMENT_TEXT = 1;
    private final int COMMENT_TEXT_WITH_REPLY = 2;

    public CommentsAdapter(final Context context, List<CommentResponse> commentResponses,
                           CommentAdapterListener listener) {
        this.context = context;
        this.commentResponseList = commentResponses;
        this.listener = listener;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = null;
        if (viewType == COMMENT_TEXT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_text_viewholder, parent, false);
        } else if (viewType == COMMENT_TEXT_WITH_REPLY) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.comment_text_with_reply_viewholder, parent, false);
        }

        return new CommentViewHolder(view);
    }

    @Override
    public int getItemViewType(int position) {
        CommentResponse commentResponse = commentResponseList.get(position);
        if (commentResponse.getCommentType().equals(AppConstant.COMMENT_TYPE_TEXT)) {
            if (commentResponse.getReplyCount() > 0) {
                return COMMENT_TEXT_WITH_REPLY;
            } else {
                return COMMENT_TEXT;
            }
        } else {
            return -1;
        }
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

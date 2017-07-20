package com.hoocons.hoocons_android.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hNguyen on 7/20/2017.
 */

public class CommentViewHolder extends ViewHolder {
    @BindView(R.id.comment_userlogo)
    ImageView mUserProfile;
    @BindView(R.id.comment_text_content)
    TextView mTextContent;
    @BindView(R.id.comment_username)
    TextView mUserName;
    @BindView(R.id.comment_time)
    TextView mCommentTime;
    @BindView(R.id.comment_like_action)
    TextView mLikeBtn;
    @BindView(R.id.comment_reply_action)
    TextView mReplyBtn;
    @BindView(R.id.comment_seemore)
    TextView mSeemoreBtn;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(itemView);
    }
}
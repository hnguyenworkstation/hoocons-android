package com.hoocons.hoocons_android.ViewHolders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.hoocons.hoocons_android.CustomUI.CustomTextView;
import com.hoocons.hoocons_android.CustomUI.RoundedCornersTransformation;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Interface.CommentAdapterListener;
import com.hoocons.hoocons_android.Networking.Responses.CommentResponse;
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
    CustomTextView mTextContent;
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
    @BindView(R.id.comment_like_count_view)
    LinearLayout mLikeView;
    @BindView(R.id.comment_like_count)
    TextView mLikeCount;

    @Nullable
    @BindView(R.id.view_replies_action)
    TextView mViewReplyBtn;

    public CommentViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initViewHolder(final CommentResponse response, final Context context,
                               final int position, final CommentAdapterListener listener) {
        loadUserProfile(context, response.getCommentBy().getProfileUrl());
        loadTextContent(response.getTextContent());
        loadCommentStats(response);
    }

    private void loadUserProfile(final Context context, final String url) {
        Glide.with(context)
                .load(url)
                .apply(RequestOptions.bitmapTransform(new RoundedCornersTransformation(context, 12, 0)))
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.noAnimation())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        return false;
                    }
                }).into(mUserProfile);
    }

    private void loadTextContent(final String textContent) {
        mTextContent.setContent(textContent);
    }

    private void loadCommentStats(final CommentResponse response) {
        mUserName.setText(response.getCommentBy().getDisplayName());
        mCommentTime.setText(AppUtils.convDate(response.getCreatedAt()));
    }
}

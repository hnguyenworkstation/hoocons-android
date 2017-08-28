package com.hoocons.hoocons_android.ViewHolders;

import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.hoocons.hoocons_android.Interface.OnFriendConnectionAdapterListener;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Networking.Responses.RelationshipResponse;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 8/26/17.
 */

public class FriendConnectionViewHolder extends ViewHolder {
    @BindView(R.id.user_profile)
    ImageView mUserProfile;

    @BindView(R.id.user_name)
    TextView mUserDisplayName;

    @BindView(R.id.location)
    TextView mLocation;

    @BindView(R.id.time_frame)
    TextView mTimeFrame;

    @BindView(R.id.more_action)
    ImageButton mMoreAction;

    public FriendConnectionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initHolder(RelationshipResponse relationshipResponse,
                           OnFriendConnectionAdapterListener listener,
                           final int position) {
        loadUserProfile(relationshipResponse.getWithUser().getProfileUrl());
    }

    private void loadUserProfile(String url) {
        assert mUserProfile != null;

        BaseApplication.getInstance().getGlide()
                .load(url)
                .apply(RequestOptions.circleCropTransform())
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
                })
                .into(mUserProfile);
    }

    public void onViewRecycled() {
        BaseApplication.getInstance().getGlide().clear(mUserProfile);
    }
}

package com.hoocons.hoocons_android.ViewHolders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
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
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Networking.Responses.FriendshipRequestResponse;
import com.hoocons.hoocons_android.R;
import com.vstechlab.easyfonts.EasyFonts;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 8/26/17.
 */

public class FriendRequestViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.user_profile)
    ImageView mUserProfile;
    @BindView(R.id.user_name)
    TextView mUserDisplayName;
    @BindView(R.id.message)
    TextView mMessage;
    @BindView(R.id.deny_action)
    ImageButton mDenyAction;
    @BindView(R.id.accept_action)
    ImageButton mAcceptAction;

    public FriendRequestViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initHolder(final Context context, FriendshipRequestResponse friendshipRequestResponse, final int position) {
        loadUserProfile(friendshipRequestResponse.getUser().getProfileUrl());

        mUserDisplayName.setTypeface(EasyFonts.robotoBold(context));
        mMessage.setTypeface(EasyFonts.robotoRegular(context));

        mUserDisplayName.setText(friendshipRequestResponse.getUser().getDisplayName());
        if (friendshipRequestResponse.getMessage() != null && friendshipRequestResponse.getMessage().length() > 0) {
            mMessage.setText(friendshipRequestResponse.getMessage());
        } else {
            mMessage.setText(context.getResources().getString(R.string.let_be_friend));
        }
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
}

package com.hoocons.hoocons_android.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
    ImageButton mTimeFrame;
    @BindView(R.id.more_action)
    ImageButton mMoreAction;

    public FriendConnectionViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

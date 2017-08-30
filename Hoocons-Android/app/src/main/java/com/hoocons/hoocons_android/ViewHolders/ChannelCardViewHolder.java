package com.hoocons.hoocons_android.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.R;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 8/30/17.
 */

public class ChannelCardViewHolder extends ViewHolder {
    @BindView(R.id.wallpaper)
    AdjustableImageView mWallPaper;
    @BindView(R.id.channel_profile)
    ImageView mChannelProfile;
    @BindView(R.id.follow_button)
    LinearLayout mFollowButton;
    @BindView(R.id.channel_name)
    TextView mChannelName;
    @BindView(R.id.channel_desc)
    TextView mChannelDesc;
    @BindView(R.id.location)
    TextView mLocation;
    @BindView(R.id.fav_count)
    TextView mFavoriteCount;


    public ChannelCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void init() {

    }
}

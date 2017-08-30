package com.hoocons.hoocons_android.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Util;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.Managers.BaseApplication;
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
        loadWallpaper("http://img.allw.mn/content/2013/09/22201006_3490.jpg");
        loadProfile("https://shechive.files.wordpress.com/2012/06/a-cute-cartoons-6.jpg?quality=100&strip=info");
    }

    private void loadWallpaper(String url) {
        BaseApplication.getInstance().getGlide()
                .load(url)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .into(mWallPaper);
    }

    private void loadProfile(String url) {
        BaseApplication.getInstance().getGlide()
                .load(url)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .into(mChannelProfile);
    }

    public void onViewRecycled() {
        BaseApplication.getInstance().getGlide().clear(mWallPaper);
        BaseApplication.getInstance().getGlide().clear(mChannelProfile);
    }
}

package com.hoocons.hoocons_android.ViewHolders;

import android.content.Context;
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
import com.hoocons.hoocons_android.Networking.Responses.ChannelProfileResponse;
import com.hoocons.hoocons_android.R;
import com.vstechlab.easyfonts.EasyFonts;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 8/30/17.
 */

public class ChannelLargeCardViewHolder extends ViewHolder {
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


    public ChannelLargeCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void init(Context context, ChannelProfileResponse response, int pos) {
        if (response != null) {
            initTypeFace(context);
            loadWallpaper(response.getProfileUrl());
            loadProfile(response.getWallpaperUrl());
        }
    }

    private void initTypeFace(Context context) {
        mChannelName.setTypeface(EasyFonts.robotoRegular(context));
        mChannelDesc.setTypeface(EasyFonts.robotoRegular(context));
        mLocation.setTypeface(EasyFonts.robotoRegular(context));
        mFavoriteCount.setTypeface(EasyFonts.robotoRegular(context));
    }

    private void loadWallpaper(String url) {
        if (url != null)
            BaseApplication.getInstance().getGlide()
                    .load(url)
                    .apply(RequestOptions.centerCropTransform())
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                    .apply(RequestOptions.noAnimation())
                    .into(mWallPaper);
    }

    private void loadProfile(String url) {
        if (url != null)
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

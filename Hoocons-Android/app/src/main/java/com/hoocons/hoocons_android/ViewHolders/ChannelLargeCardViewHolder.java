package com.hoocons.hoocons_android.ViewHolders;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.util.Util;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.Interface.OnChannelProfileClickListener;
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
    @BindView(R.id.cardroot)
    RelativeLayout rootView;

    @BindView(R.id.wallpaper)
    AdjustableImageView mWallPaper;
    @BindView(R.id.channel_name)
    TextView mChannelName;
    @BindView(R.id.channel_cat)
    TextView mChannelCat;
    @BindView(R.id.location)
    TextView mLocation;
    @BindView(R.id.fav_count)
    TextView mFavoriteCount;


    public ChannelLargeCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void init(Context context, ChannelProfileResponse response, int pos, OnChannelProfileClickListener listener) {
        if (response != null) {
            initTypeFace(context);
            initClickListener(pos, listener);

            loadWallpaper(response.getProfileUrl());
        }
    }

    private void initClickListener(final int position, final OnChannelProfileClickListener listener) {
        rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onChannelProfileClicked(position);
            }
        });

        rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                listener.onChannelProfileLongClicked(position);
                return false;
            }
        });
    }

    private void initTypeFace(Context context) {
        mChannelName.setTypeface(EasyFonts.robotoBold(context));
        mChannelCat.setTypeface(EasyFonts.robotoRegular(context));
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

    public void onViewRecycled() {
        BaseApplication.getInstance().getGlide().clear(mWallPaper);
    }
}

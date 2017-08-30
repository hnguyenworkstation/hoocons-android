package com.hoocons.hoocons_android.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 8/30/17.
 */

public class EventCardViewHolder extends ViewHolder {
    @BindView(R.id.single_image)
    ImageView mSingleImage;


    public EventCardViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initView() {
        loadSingleImage("http://img.allw.mn/content/2013/09/22201006_3490.jpg");
    }

    private void loadSingleImage(String url) {
        BaseApplication.getInstance().getGlide()
                .load(url)
                .apply(RequestOptions.centerCropTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .into(mSingleImage);
    }

    public void onViewRecycled() {
        BaseApplication.getInstance().getGlide().clear(mSingleImage);
    }
}

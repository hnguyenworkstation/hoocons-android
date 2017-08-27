package com.hoocons.hoocons_android.ViewHolders;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 8/27/17.
 */
public class HotAdPanelViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.panel_image)
    AdjustableImageView mPanelImage;
    @BindView(R.id.ad_title)
    TextView mTitle;
    @BindView(R.id.ad_message)
    TextView mMessage;
    @BindView(R.id.count)
    TextView mCount;

    public HotAdPanelViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }
}

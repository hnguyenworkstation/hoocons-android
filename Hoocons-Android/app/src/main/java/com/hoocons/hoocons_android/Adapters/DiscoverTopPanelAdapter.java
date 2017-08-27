package com.hoocons.hoocons_android.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.HotAdPanelViewHolder;

/**
 * Created by hungnguyen on 8/27/17.
 */

public class DiscoverTopPanelAdapter extends RecyclerView.Adapter<HotAdPanelViewHolder> {
    public DiscoverTopPanelAdapter() {
    }

    @Override
    public HotAdPanelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hot_ad_panel_viewholder, parent, false);
        return new HotAdPanelViewHolder(view);
    }

    @Override
    public void onBindViewHolder(HotAdPanelViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 10;
    }
}

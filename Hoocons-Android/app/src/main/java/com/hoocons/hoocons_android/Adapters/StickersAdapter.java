package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Interface.OnStickerClickListener;
import com.hoocons.hoocons_android.Models.Sticker;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.StickerViewHolder;

import java.util.List;

/**
 * Created by hungnguyen on 7/27/17.
 */

public class StickersAdapter extends RecyclerView.Adapter<StickerViewHolder> {
    private Context context;
    private OnStickerClickListener listener;
    private List<Sticker> stickerList;

    public StickersAdapter(Context context, OnStickerClickListener listener, List<Sticker> stickerList) {
        this.context = context;
        this.listener = listener;
        this.stickerList = stickerList;
    }


    @Override
    public StickerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sticker_viewholder, parent, false);
        return new StickerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StickerViewHolder holder, int position) {
        holder.initStickerHolder(context, stickerList.get(position), position, listener);
    }

    @Override
    public int getItemCount() {
        return stickerList.size();
    }
}

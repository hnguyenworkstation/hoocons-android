package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.SquaredImageViewHolder;

import java.util.ArrayList;

/**
 * Created by hNguyen on 6/19/2017.
 */
public class ImageLoaderAdapter extends RecyclerView.Adapter<SquaredImageViewHolder> {
    private ArrayList<String> imageList;
    private Context context;
    private final int MAX_ITEMS = 6;

    public ImageLoaderAdapter(Context context, ArrayList<String> imageList) {
        this.imageList = imageList;
        this.context =context;
    }

    @Override
    public SquaredImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_square_image,
                parent, false);
        return new SquaredImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SquaredImageViewHolder holder, int position) {
        holder.initImage(context, imageList.get(position), position, position == MAX_ITEMS - 1, imageList.size());
    }

    @Override
    public int getItemCount() {
        if (imageList.size() > MAX_ITEMS)
            return MAX_ITEMS;

        return imageList.size();
    }
}

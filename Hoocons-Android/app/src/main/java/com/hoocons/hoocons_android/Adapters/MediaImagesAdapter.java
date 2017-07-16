package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Networking.Responses.MediaResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.MediaImageViewHolder;
import com.hoocons.hoocons_android.ViewHolders.SquaredImageViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungnguyen on 7/16/17.
 */

public class MediaImagesAdapter extends RecyclerView.Adapter<MediaImageViewHolder> {
    private List<MediaResponse> mediaResponses;
    private Context context;
    private final int MAX_ITEMS = 9;

    public MediaImagesAdapter(Context context, List<MediaResponse> mediaResponses) {
        this.mediaResponses = mediaResponses;
        this.context =context;
    }

    @Override
    public MediaImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_square_image,
                        parent, false);
        return new MediaImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MediaImageViewHolder holder, int position) {
        holder.initImage(context, mediaResponses.get(position).getUrl(),
                position, position == MAX_ITEMS - 1, mediaResponses.size());
    }

    @Override
    public int getItemCount() {
        if (mediaResponses.size() >= MAX_ITEMS)
            return MAX_ITEMS;

        return mediaResponses.size();
    }
}

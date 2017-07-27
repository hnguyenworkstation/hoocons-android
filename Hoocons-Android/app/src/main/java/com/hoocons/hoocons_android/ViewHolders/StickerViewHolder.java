package com.hoocons.hoocons_android.ViewHolders;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.hoocons.hoocons_android.Interface.OnStickerClickListener;
import com.hoocons.hoocons_android.Models.Sticker;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 7/27/17.
 */

public class StickerViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.sticker_view)
    ImageView mStickerView;
    @BindView(R.id.sticker_progress)
    ProgressBar mStickerProgress;

    private GifDrawable mGifDrawable;

    public StickerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initStickerHolder(final Context context, final Sticker sticker,
                                  final int position, final OnStickerClickListener listener) {
        Glide.with(context)
                .load(sticker.getValue())
                .apply(RequestOptions.fitCenterTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE))
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model,
                                                Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model,
                                                   Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        if (resource instanceof GifDrawable) {
                            mGifDrawable = (GifDrawable) resource;
                        }

                        mStickerProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mStickerView);

        mStickerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onStickerClick(position);
            }
        });

        mStickerView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listener.onStickerLongClick(position);
                return false;
            }
        });
    }
}

package com.hoocons.hoocons_android.ViewHolders;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.CustomUI.SquareImageView;
import com.hoocons.hoocons_android.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hungnguyen on 7/16/17.
 */

public class MediaImageViewHolder extends RecyclerView.ViewHolder {
    @BindView(R.id.image_holder)
    AdjustableImageView mImageView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.image_root)
    SquareImageView mImageRoot;
    @BindView(R.id.image_filter)
    RelativeLayout mImageFilter;
    @BindView(R.id.num_cover)
    TextView mNumCovered;

    private final BaseSpringSystem mSpringSystem = SpringSystem.create();
    private final ImageSpringListener springListener = new ImageSpringListener();
    private Spring mScaleSpring;

    private int position;

    public MediaImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initImage(Context context, String url, final int position,
                          final boolean isLast, final int listSize) {
        this.position = position;
        mScaleSpring = mSpringSystem.createSpring();
        mScaleSpring.addListener(springListener);

        mImageRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mScaleSpring.setEndValue(0.3);
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        mScaleSpring.setEndValue(0);
                        break;
                }
                return true;
            }
        });

        Glide.with(context)
                .load(url)
                .centerCrop()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.e("Viewholder", "onResourceReady: complete");
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mImageView);

        if (isLast) {
            if ((listSize-position-1) > 0)
                mImageFilter.setVisibility(View.VISIBLE);
                String num = String.format("+%s", String.valueOf(listSize-position-1));
                mNumCovered.setText(num);
        }
    }

    private class ImageSpringListener extends SimpleSpringListener {
        @Override
        public void onSpringUpdate(Spring spring) {
            float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(),
                    0, 1, 1, 0.5);
            mImageRoot.setScaleX(mappedValue);
            mImageRoot.setScaleY(mappedValue);
        }
    }
}


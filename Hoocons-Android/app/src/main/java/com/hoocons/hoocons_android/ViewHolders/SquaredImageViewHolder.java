package com.hoocons.hoocons_android.ViewHolders;

import android.content.Context;
import android.media.Image;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.CustomUI.SquareImageView;
import com.hoocons.hoocons_android.R;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hNguyen on 6/19/2017.
 */
public class SquaredImageViewHolder extends RecyclerView.ViewHolder {
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

    private int position;

    public SquaredImageViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    public void initImage(Context context, String imageLink, final int position, final boolean isLast, final int listSize) {
        this.position = position;
        File file = new File(imageLink);
        Uri imageUri = Uri.fromFile(file);
        Log.e("Test", imageUri.toString());

        Glide.with(context)
                .load(imageUri)
                .fitCenter()
                .crossFade()
                .listener(new RequestListener<Uri, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, Uri model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, Uri model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        Log.e("Viewholder", "onResourceReady: complete");
                        mProgressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mImageView);

        if (isLast) {
            mImageFilter.setVisibility(View.VISIBLE);
            String num = String.format("+%s", String.valueOf(listSize-position-1));
            mNumCovered.setText(num);
        }
    }

}

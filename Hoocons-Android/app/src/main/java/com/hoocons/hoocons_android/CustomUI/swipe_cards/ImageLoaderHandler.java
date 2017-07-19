package com.hoocons.hoocons_android.CustomUI.swipe_cards;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.ImageViewTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;

import java.util.WeakHashMap;

/**
 * Created by hNguyen on 6/19/2017.
 */
public class ImageLoaderHandler {
    private static ImageLoaderHandler sInstance;

    private WeakHashMap<String, Target> mPreloadQueue = new WeakHashMap<>();

    private ImageLoaderHandler() {
    }

    public static ImageLoaderHandler get() {
        if (sInstance == null) {
            sInstance = new ImageLoaderHandler();
        }
        return sInstance;
    }

    public void loadCardImage(Activity activity, ImageView iv, final View loadingView, final String url, final boolean isShowLoadWhenStarted) {
        Glide.with(activity)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(new ImageViewTarget<Drawable>(iv) {
                    @Override
                    protected void setResource(@Nullable Drawable resource) {
                        getView().setImageDrawable(resource);
                    }

                    @Override
                    public void onLoadStarted(@Nullable Drawable placeholder) {
                        super.onLoadStarted(placeholder);
                        if (isShowLoadWhenStarted && loadingView != null) {
                            loadingView.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {
                        super.onLoadCleared(placeholder);
                    }

                    @Override
                    public void onLoadFailed(@Nullable Drawable errorDrawable) {
                        super.onLoadFailed(errorDrawable);
                        if (loadingView != null) {
                            loadingView.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onResourceReady(Drawable resource, @Nullable Transition<? super Drawable> transition) {
                        super.onResourceReady(resource, transition);
                        if (loadingView != null) {
                            loadingView.setVisibility(View.GONE);
                        }
                    }
                });
    }

}

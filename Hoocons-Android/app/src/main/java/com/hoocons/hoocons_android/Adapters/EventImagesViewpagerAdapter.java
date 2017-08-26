package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.chrisbanes.photoview.PhotoView;
import com.hoocons.hoocons_android.EventBus.OnImageViewClicked;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Networking.Responses.MediaResponse;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import me.iwf.photopicker.entity.Photo;

/**
 * Created by hungnguyen on 7/23/17.
 */
public class EventImagesViewpagerAdapter extends PagerAdapter {
    private final List<MediaResponse> mediaResponses;
    private Context context;

    public EventImagesViewpagerAdapter(final Context context, final List<MediaResponse> mediaResponses) {
        this.mediaResponses = mediaResponses;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mediaResponses.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.full_screen_image_viewholder,
                container, false);

        PhotoView photoView = (PhotoView) view.findViewById(R.id.photo_view);
        final ProgressBar progressBar = (ProgressBar) view.findViewById(R.id.full_image_progress);

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new OnImageViewClicked());
            }
        });

        BaseApplication.getInstance().getGlide()
                .load(mediaResponses.get(position).getUrl())
                .apply(RequestOptions.fitCenterTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(photoView);

        // Now just add PhotoView to ViewPager and return it
        container.addView(view, ViewPager.LayoutParams.MATCH_PARENT,
                ViewPager.LayoutParams.MATCH_PARENT);

        return view;
    }


    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}

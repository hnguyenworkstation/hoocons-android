package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.chrisbanes.photoview.PhotoView;
import com.hoocons.hoocons_android.Networking.Responses.MediaResponse;
import com.hoocons.hoocons_android.R;

import java.util.List;

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
        PhotoView photoView = new PhotoView(container.getContext());
        Glide.with(context)
                .load(mediaResponses.get(position).getUrl())
                .apply(RequestOptions.fitCenterTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .apply(RequestOptions.placeholderOf(R.drawable.ab_progress))
                .into(photoView);

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

        return photoView;
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

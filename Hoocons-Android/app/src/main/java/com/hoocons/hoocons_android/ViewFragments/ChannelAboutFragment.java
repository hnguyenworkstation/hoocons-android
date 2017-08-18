package com.hoocons.hoocons_android.ViewFragments;


import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.hoocons.hoocons_android.Activities.UserProfileActivity;
import com.hoocons.hoocons_android.CustomUI.view.ViewHelper;
import com.hoocons.hoocons_android.EventBus.AllowSlideDown;
import com.hoocons.hoocons_android.EventBus.BlockSlideDown;
import com.hoocons.hoocons_android.Interface.InfiniteScrollListener;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChannelAboutFragment extends Fragment implements
        ObservableScrollViewCallbacks, View.OnClickListener {
    @BindView(R.id.custom_toolbar)
    RelativeLayout mCustomToolbar;
    @BindView(R.id.small_profile)
    RelativeLayout mSmallProfile;
    @BindView(R.id.obs_scrollview)
    ObservableRecyclerView mRecyclerView;

    @BindView(R.id.small_profile_header)
    ImageView mSmallProfileImage;
    @BindView(R.id.wallpaper_image)
    ImageView mWallpaperImage;

    @BindView(R.id.action_back)
    ImageButton mActionBack;
    @BindView(R.id.action_more)
    ImageButton mActionMore;

    @BindView(R.id.wallpaper_progress_bar)
    ProgressBar mWallpaperProgress;
    @BindView(R.id.small_profile_progress_bar)
    ProgressBar mSmallProfileProgress;

    @BindView(R.id.linear)
    View mLinear;
    @BindView(R.id.user_display_name)
    TextView mActionBarDisplayName;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private boolean canLoadMore = false;
    private boolean isLoading = false;

    private String mParam1;
    private String mParam2;

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private final String TAG = UserProfileActivity.class.getSimpleName();

    private View mOverlayView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;
    private boolean isMySelf;
    private Intent mIntent;
    private Handler handler;

    public ChannelAboutFragment() {

    }

    public static ChannelAboutFragment newInstance(String param1, String param2) {
        ChannelAboutFragment fragment = new ChannelAboutFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_channel_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initGeneralView(view);
        initChannelAbout();
    }

    private void initGeneralView(View view) {
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = 148;

        mCustomToolbar.bringToFront();
        mOverlayView = view.findViewById(R.id.overlay);
        mRecyclerView.setScrollViewCallbacks(this);

        ScrollUtils.addOnGlobalLayoutListener(mRecyclerView, new Runnable() {
            @Override
            public void run() {
                mRecyclerView.scrollTo(mFlexibleSpaceImageHeight, 0);
            }
        });

        mActionBack.setOnClickListener(this);
        mActionMore.setOnClickListener(this);
        mWallpaperImage.setOnClickListener(this);
    }

    private void initChannelAbout() {
        loadWallPaperImage("https://c1.staticflickr.com/1/256/19767218293_aa4a9248d3.jpg");

        initEventRecyclerView();
    }

    private void initEventRecyclerView() {
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(null);
        mRecyclerView.addOnScrollListener(new InfiniteScrollListener((LinearLayoutManager) mLayoutManager) {

            @Override
            protected void loadMoreItems() {
                isLoading = true;
                if (canLoadMore) {
                    // loadMoreEvents();
                }
            }

            @Override
            public int getTotalItems() {
                return 0;
            }

            @Override
            public boolean isLastItem() {
//                return ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition()
//                        == (mEventsAdapter.getItemCount() - 1);
                return false;
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void loadWallPaperImage(String url) {
        Glide.with(this)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mWallpaperImage.setVisibility(View.VISIBLE);
                        mWallpaperProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mWallpaperImage);
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        if (scrollY == 0) {
            EventBus.getDefault().post(new AllowSlideDown());
        } else {
            EventBus.getDefault().post(new BlockSlideDown());
        }

        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mWallpaperImage, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);

        Log.i(TAG, String.format("onScrollChanged: %s", String.valueOf(scrollY)));

        if (scrollY >= 480) {
            mCustomToolbar.setBackgroundColor(getResources().getColor(R.color.white));
            mSmallProfile.setVisibility(View.VISIBLE);
            mActionBarDisplayName.setVisibility(View.VISIBLE);
            mLinear.setVisibility(View.VISIBLE);
        } else {
            mCustomToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
            mSmallProfile.setVisibility(View.GONE);
            mLinear.setVisibility(View.GONE);
            mActionBarDisplayName.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}

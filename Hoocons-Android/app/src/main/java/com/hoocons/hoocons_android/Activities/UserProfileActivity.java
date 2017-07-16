package com.hoocons.hoocons_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.birbit.android.jobqueue.JobManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ppamorim.dragger.DraggerActivity;
import com.hoocons.hoocons_android.Adapters.EventsAdapter;
import com.hoocons.hoocons_android.CustomUI.GlideCircleTransformation;
import com.hoocons.hoocons_android.CustomUI.view.ViewHelper;
import com.hoocons.hoocons_android.EventBus.FetchEventListSuccessEvBusRequest;
import com.hoocons.hoocons_android.EventBus.FetchUserInfoCompleteEvBusRequest;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.FetchCreatedEventJob;
import com.hoocons.hoocons_android.Tasks.Jobs.GetSelfInfoJob;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProfileActivity extends DraggerActivity implements ObservableScrollViewCallbacks {
    @BindView(R.id.custom_toolbar)
    RelativeLayout mCustomToolbar;
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

    @BindView(R.id.user_display_name)
    TextView mActionBarDisplayName;

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private final String TAG = UserProfileActivity.class.getSimpleName();

    private View mOverlayView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;
    private boolean isMySelf;
    private Intent mIntent;

    private final String USERID = "USERID";
    private final String USER_DISPLAY_NAME = "USER_DISPLAY_NAME";
    private final String USER_NICKNAME = "USER_NICKNAME";
    private final String IS_FRIEND = "IS_FRIEND";
    private final String USER_PROFILE_URL = "USER_PROFILE_URL";
    private final String MYSELF = "IS_MY_SELF";

    private final JobManager jobManager = BaseApplication.getInstance().getJobManager();
    private List<EventResponse> eventResponseList;
    private final int EVENT_PACK = 15;
    private boolean canLoadMore = true;

    private EventsAdapter mEventsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        mIntent = getIntent();
        isMySelf = mIntent.getBooleanExtra(MYSELF, false);

        eventResponseList = new ArrayList<>();
        mEventsAdapter = new EventsAdapter(this, eventResponseList, isMySelf);

        initGeneralView();

        if (isMySelf) {
            jobManager.addJobInBackground(new GetSelfInfoJob());
            jobManager.addJobInBackground(new FetchCreatedEventJob(eventResponseList.size(),
                    eventResponseList.size() + EVENT_PACK));
        }

        initEventRecyclerView();
    }

    private void initEventRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mEventsAdapter);
    }

    private void loadActionBarProfileImage(String url) {
        Glide.with(this)
                .load(url)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .transform(new GlideCircleTransformation(this))
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        mSmallProfileImage.setVisibility(View.VISIBLE);
                        mSmallProfileProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mSmallProfileImage);
    }

    private void initGeneralView() {
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = 148;

        mCustomToolbar.bringToFront();
        mOverlayView = findViewById(R.id.overlay);
        mRecyclerView.setScrollViewCallbacks(this);

        setTitle(null);
        ScrollUtils.addOnGlobalLayoutListener(mRecyclerView, new Runnable() {
            @Override
            public void run() {
                mRecyclerView.scrollTo(mFlexibleSpaceImageHeight, 0);
            }
        });
    }

    private void loadWallPaperImage(String url) {
        Glide.with(this)
                .load(url)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .crossFade()
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model,
                                               Target<GlideDrawable> target,
                                               boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model,
                                                   Target<GlideDrawable> target,
                                                   boolean isFromMemoryCache,
                                                   boolean isFirstResource) {
                        mWallpaperImage.setVisibility(View.VISIBLE);
                        mWallpaperProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mWallpaperImage);
    }

    private void initViewWithCompleteInfo(UserInfoResponse info) {
        loadWallPaperImage("https://c1.staticflickr.com/1/256/19767218293_aa4a9248d3.jpg");

        assert mActionBarDisplayName != null;
        mActionBarDisplayName.setText(info.getDisplayName());

        loadActionBarProfileImage(info.getProfileUrl());
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        setSlideEnabled(scrollY == 0);

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
            mCustomToolbar.setVisibility(View.VISIBLE);
        } else {
            mCustomToolbar.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    /**********************************************
     * EVENTBUS CATCHING FIELDS
     *  + PublicModeRequest: Request Public Mode
     ***********************************************/
    @Subscribe
    public void onEvent(FetchUserInfoCompleteEvBusRequest request) {
        initViewWithCompleteInfo(request.getmResponse());
    }

    @Subscribe
    public void onEvent(FetchEventListSuccessEvBusRequest request) {
        if (request.getResponseList().size() < EVENT_PACK) {
            canLoadMore = false;
        }

        eventResponseList.addAll(request.getResponseList());
        mEventsAdapter.notifyItemRangeInserted(eventResponseList.size() - eventResponseList.size() - 1,
                eventResponseList.size() - 1);
    }
}

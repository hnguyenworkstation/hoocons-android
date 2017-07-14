package com.hoocons.hoocons_android.Activities;

import android.content.Intent;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.beardedhen.androidbootstrap.BootstrapButton;
import com.beardedhen.androidbootstrap.BootstrapCircleThumbnail;
import com.birbit.android.jobqueue.JobManager;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.github.ppamorim.dragger.DraggerActivity;
import com.hoocons.hoocons_android.CustomUI.GlideCircleTransformation;
import com.hoocons.hoocons_android.CustomUI.view.ViewHelper;
import com.hoocons.hoocons_android.CustomUI.view.ViewPropertyAnimator;
import com.hoocons.hoocons_android.EventBus.FetchUserInfoCompleteEvBusRequest;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.GetSelfInfoJob;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProfileActivity extends DraggerActivity implements ObservableScrollViewCallbacks {
    @BindView(R.id.custom_toolbar)
    RelativeLayout mCustomToolbar;
    @BindView(R.id.obs_scrollview)
    ObservableScrollView mScrollView;

    @BindView(R.id.profile_header)
    ImageView mProfileImage;
    @BindView(R.id.small_profile_header)
    ImageView mSmallProfileImage;
    @BindView(R.id.wallpaper_image)
    ImageView mWallpaperImage;

    @BindView(R.id.display_name)
    TextView mDisplayName;
    @BindView(R.id.user_display_name)
    TextView mActionBarDisplayName;
    @BindView(R.id.nick_name)
    TextView mNickname;

    @BindView(R.id.action_back)
    ImageButton mActionBack;
    @BindView(R.id.action_more)
    ImageButton mActionMore;

    @BindView(R.id.action_friend_status)
    BootstrapButton mAddFriendBtn;
    @BindView(R.id.action_send_message)
    BootstrapButton mSendMessageBtn;

    @BindView(R.id.wallpaper_progress_bar)
    ProgressBar mWallpaperProgress;
    @BindView(R.id.profile_progress_bar)
    ProgressBar mProfileProgress;
    @BindView(R.id.small_profile_progress_bar)
    ProgressBar mSmallProfileProgress;

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private final String TAG = UserProfileActivity.class.getSimpleName();

    private View mImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        mIntent = getIntent();
        isMySelf = mIntent.getBooleanExtra(MYSELF, false);

        if (isMySelf) {
            jobManager.addJobInBackground(new GetSelfInfoJob());
        }

        initGeneralView();
        initDetailedView();
    }

    private void initDetailedView() {
        if (isMySelf) {
            UserInfoResponse response = SharedPreferencesManager.getDefault().getUserKeyInfo();

            // Load profile to both side
            loadProfileImage(response.getProfileUrl());
            loadActionBarProfileImage(response.getProfileUrl());

            String nickname = "@" + response.getNickname();

            mActionBarDisplayName.setText(response.getDisplayName());
            mDisplayName.setText(response.getDisplayName());
            mNickname.setText(nickname);
        }
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

    private void loadProfileImage(String url) {
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
                        mProfileImage.setVisibility(View.VISIBLE);
                        mProfileProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mProfileImage);
    }

    private void initGeneralView() {
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = 148;

        mCustomToolbar.bringToFront();
        mOverlayView = findViewById(R.id.overlay);

        mScrollView.setScrollViewCallbacks(this);
        setTitle(null);
        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(mFlexibleSpaceImageHeight, 0);
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
}

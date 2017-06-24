package com.hoocons.hoocons_android.Activities;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.hoocons.hoocons_android.CustomUI.view.ViewHelper;
import com.hoocons.hoocons_android.CustomUI.view.ViewPropertyAnimator;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProfileActivity extends BaseActivity implements ObservableScrollViewCallbacks {
    @BindView(R.id.custom_toolbar)
    RelativeLayout mCustomToolbar;

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private View mImageView;
    private View mOverlayView;
    private ObservableScrollView mScrollView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
        mActionBarSize = 148;

        mCustomToolbar.bringToFront();

        mImageView = findViewById(R.id.image);
        mOverlayView = findViewById(R.id.overlay);
        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);
        setTitle(null);

        ScrollUtils.addOnGlobalLayoutListener(mScrollView, new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(mFlexibleSpaceImageHeight, 0);
            }
        });
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mImageView, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

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
}

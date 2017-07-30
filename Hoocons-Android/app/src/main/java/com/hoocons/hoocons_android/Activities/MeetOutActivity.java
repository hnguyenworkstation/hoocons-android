package com.hoocons.hoocons_android.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.hoocons.hoocons_android.CustomUI.view.ViewHelper;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Parcel.EventParcel;
import com.hoocons.hoocons_android.Parcel.MeetOutParcel;
import com.hoocons.hoocons_android.R;

import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MeetOutActivity extends BaseActivity implements
        ObservableScrollViewCallbacks, View.OnClickListener {
    @BindView(R.id.action_back)
    ImageButton mActionBack;
    @BindView(R.id.action_more)
    ImageButton mActionMore;
    @BindView(R.id.bar_linear)
    View barLinear;
    @BindView(R.id.meetout_actionbar_name)
    TextView mMeetOutActionBarName;
    @BindView(R.id.meetout_actionbar)
    RelativeLayout mMeetOutActionbar;

    @BindView(R.id.meetout_header_area)
    RelativeLayout mHeaderArea;
    @BindView(R.id.meetout_background_image)
    ImageView mMeetOutBackgroundImage;
    @BindView(R.id.owner_image)
    ImageView mMeetOutOwnerImage;
    @BindView(R.id.progress_bar)
    ProgressBar mOwnerProgressBar;
    @BindView(R.id.meetup_header_name)
    TextView mMeetupHeaderName;

    @BindView(R.id.obs_scrollview)
    ObservableScrollView mScrollView;

    private MeetOutParcel parcel;

    // General View Layout
    private View mOverlayView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;
    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meet_out);
        ButterKnife.bind(this);

        parcel = (MeetOutParcel) Parcels.unwrap(getIntent().getParcelableExtra("meetout"));

        initGeneralView();
        initFirstView();
    }

    private void initFirstView() {
        if (parcel != null) {
            mMeetupHeaderName.setText(parcel.getMeetOutName());
            mMeetOutActionBarName.setText(parcel.getMeetOutName());

            if (parcel.getMeetOutMediaUrl() != null) {
                AppUtils.loadCropBlurImageWithProgressBar(this, parcel.getMeetOutMediaUrl(),
                        mMeetOutBackgroundImage, null);
            }
        }
    }

    private void initGeneralView() {
        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.meetout_header_image);
        mActionBarSize = 148;

        mMeetOutActionbar.bringToFront();
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

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        // Translate overlay and image
        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
        ViewHelper.setTranslationY(mHeaderArea, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));

        // Change alpha of overlay
        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));

        // Scale title text
        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);

        if (scrollY >= (flexibleRange * 2)) {
            showCustomBar();
        } else {
            hideCustomBar();
        }
    }

    private void hideCustomBar() {
        mMeetOutActionBarName.setVisibility(View.GONE);
        barLinear.setVisibility(View.GONE);
        mMeetOutActionbar.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    private void showCustomBar() {
        mMeetOutActionBarName.setVisibility(View.VISIBLE);
        barLinear.setVisibility(View.VISIBLE);
        mMeetOutActionbar.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }
}

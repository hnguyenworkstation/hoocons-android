package com.hoocons.hoocons_android.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.rebound.BaseSpringSystem;
import com.facebook.rebound.SimpleSpringListener;
import com.facebook.rebound.Spring;
import com.facebook.rebound.SpringSystem;
import com.facebook.rebound.SpringUtil;
import com.github.ksoichiro.android.observablescrollview.ObservableRecyclerView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.hoocons.hoocons_android.CustomUI.view.ViewHelper;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewHolders.SquaredImageViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewMeetOutActivity extends BaseActivity implements
        ObservableScrollViewCallbacks, View.OnClickListener {
    @BindView(R.id.obs_scrollview)
    ObservableScrollView mScrollView;
    @BindView(R.id.custom_toolbar)
    RelativeLayout mCustomToolbar;
    @BindView(R.id.header_area)
    RelativeLayout mHeaderArea;
    @BindView(R.id.custom_bar_text)
    TextView mCustomBarText;
    @BindView(R.id.linear)
    View mCustomBarLinear;
    @BindView(R.id.recycler)
    ObservableRecyclerView mImageRecycler;
    @BindView(R.id.add_image_action)
    ImageView mAddImageBtn;
    @BindView(R.id.meeting_add_time)
    TextView mAddTimeFrame;
    @BindView(R.id.meeting_time_view)
    LinearLayout mMeetingTimeView;
    @BindView(R.id.meeting_date_time)
    TextView mDateTime;
    @BindView(R.id.meeting_clock_time)
    TextView mClockTime;

    @BindView(R.id.submit_new_meeting)
    Button mSubmitMeeting;

    private View mOverlayView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    private final BaseSpringSystem mSpringSystem = SpringSystem.create();
    private final ImageSpringListener springListener = new ImageSpringListener();
    private Spring mScaleSpring;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_meeting);
        ButterKnife.bind(this);

        initGeneralView();
        initOnClickListener();
    }

    private void initOnClickListener() {
        mSubmitMeeting.setOnClickListener(this);
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

        // Init submit button affect
        // Create the animation spring.
        mScaleSpring = mSpringSystem.createSpring();
        mScaleSpring.addListener(springListener);

        mSubmitMeeting.setOnTouchListener(new View.OnTouchListener() {
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

        Log.i(TAG, String.format("onScrollChanged: %s", String.valueOf(scrollY)));

        if (scrollY >= 480) {
            showCustomBar();
        } else {
            hideCustomBar();
        }
    }

    private void hideCustomBar() {
        mCustomBarText.setVisibility(View.GONE);
        mCustomBarLinear.setVisibility(View.GONE);
        mCustomToolbar.setBackgroundColor(getResources().getColor(R.color.transparent));
    }

    private void showCustomBar() {
        mCustomBarText.setVisibility(View.VISIBLE);
        mCustomBarLinear.setVisibility(View.VISIBLE);
        mCustomToolbar.setBackgroundColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onDownMotionEvent() {

    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_new_meeting:
                break;
            default:
                break;
        }
    }

    private class ImageSpringListener extends SimpleSpringListener {
        @Override
        public void onSpringUpdate(Spring spring) {
            // On each update of the spring value, we adjust the scale of the image view to match the
            // springs new value. We use the SpringUtil linear interpolation function mapValueFromRangeToRange
            // to translate the spring's 0 to 1 scale to a 100% to 50% scale range and apply that to the View
            // with setScaleX/Y. Note that rendering is an implementation detail of the application and not
            // Rebound itself. If you need Gingerbread compatibility consider using NineOldAndroids to update
            // your view properties in a backwards compatible manner.
            float mappedValue = (float) SpringUtil.mapValueFromRangeToRange(spring.getCurrentValue(), 0, 1, 1, 0.5);
            mSubmitMeeting.setScaleX(mappedValue);
            mSubmitMeeting.setScaleY(mappedValue);
        }
    }
}

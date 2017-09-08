package com.hoocons.hoocons_android.Activities;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.hoocons.hoocons_android.CustomUI.AdjustableImageView;
import com.hoocons.hoocons_android.CustomUI.CustomFlowLayout;
import com.hoocons.hoocons_android.CustomUI.CustomTextView;
import com.hoocons.hoocons_android.CustomUI.view.ViewHelper;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.FetchCompleteMeetOutFailure;
import com.hoocons.hoocons_android.EventBus.FetchCompleteMeetOutSuccess;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.MapUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Models.Topic;
import com.hoocons.hoocons_android.Networking.Responses.CompleteMeetOutResponse;
import com.hoocons.hoocons_android.Parcel.EventParcel;
import com.hoocons.hoocons_android.Parcel.MeetOutParcel;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.FetchCompleteMeetOutById;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.List;

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

    // Meetout content body
    @BindView(R.id.content_view)
    LinearLayout mContentView;
    @BindView(R.id.content_loading_prog)
    ProgressBar mContentLoadingProgress;
    @BindView(R.id.meetup_name)
    TextView mMeetOutName;
    @BindView(R.id.meetup_desc)
    CustomTextView mMeetOutDesc;
    @BindView(R.id.meeting_from_timestamp)
    TextView mFromDateTime;
    @BindView(R.id.meeting_to_timestamp)
    TextView mToDateTime;
    @BindView(R.id.meeting_location_name)
    TextView mMeetOutLocationName;
    @BindView(R.id.meeting_location_address)
    TextView mMeetOutLocationAddress;
    @BindView(R.id.meetup_location_img)
    AdjustableImageView mLocationImage;
    @BindView(R.id.meetup_loc_prog)
    ProgressBar mMeetOutMapViewProgress;

    @BindView(R.id.topic_flow_layout)
    CustomFlowLayout mFlowLayout;

    private MeetOutParcel parcel;

    // General View Layout
    private View mOverlayView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;
    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        setContentView(R.layout.activity_meet_out);
        ButterKnife.bind(this);

        parcel = (MeetOutParcel) Parcels.unwrap(getIntent().getParcelableExtra("meetout"));
        if (parcel != null) {
            BaseApplication.getInstance().getJobManager()
                    .addJobInBackground(new FetchCompleteMeetOutById(parcel.getId()));
        }

        initGeneralView();
        initFirstView();
    }

    private void initViewWithMeetOutData(CompleteMeetOutResponse response) {
        loadOwnerImage(response.getCreatedBy().getProfileUrl());

        // Show data
        mContentLoadingProgress.setVisibility(View.GONE);
        mContentView.setVisibility(View.VISIBLE);

        mMeetOutName.setText(response.getName());
        mMeetOutDesc.setContent(response.getDescription());

        mFromDateTime.setText(AppUtils.convertDateTimeFromUTC(response.getFromDateTime()));
        mToDateTime.setText(AppUtils.convertDateTimeFromUTC(response.getToDateTime()));

        mMeetOutLocationName.setText(response.getMeetupLocationName());
        mMeetOutLocationAddress.setText(response.getMeetupLocationAddress());

        loadLocationMapView(MapUtils.getMapLocationUrl(String.valueOf(response.getMeetupLocation().getLatitude()),
                String.valueOf(response.getMeetupLocation().getLongitude())));

        initFlowLayoutView(response.getTopics());
    }

    private void loadOwnerImage(final String url) {
        AppUtils.loadCircleImage(this, url, mMeetOutOwnerImage, mOwnerProgressBar);
    }

    private void initFlowLayoutView(final List<Topic> topicList) {
        mFlowLayout.removeAllViews();
        mFlowLayout.setVisibility(View.VISIBLE);

        for (int i = 0; i < topicList.size(); i++) {
            final RelativeLayout item = (RelativeLayout) getLayoutInflater().inflate(R.layout.topic_flow_layout,
                    mFlowLayout, false);
            final TextView topic = (TextView) item.findViewById(R.id.topic_flow_text);
            final ImageView closeicon =
                    (ImageView) item.findViewById(R.id.topic_flow_close);
            closeicon.setVisibility(View.GONE);

            topic.setText(topicList.get(i).getTopicName());
            item.setTag(topicList.get(i).getId());

            mFlowLayout.addView(item);
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int tag = (int) v.getTag();
                }
            });
        }
    }

    private void loadLocationMapView(String url) {
        Glide.with(this)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .apply(RequestOptions.centerCropTransform())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        mMeetOutMapViewProgress.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(mLocationImage);
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
        Log.d(TAG, "onScrollChanged: " + String.valueOf(scrollY));

        if (scrollY >= 500) {
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

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    /**********************************************
     * EVENTBUS CATCHING FIELDS
     *  +
     ***********************************************/
    @Subscribe
    public void onEvent(FetchCompleteMeetOutSuccess request) {
        initViewWithMeetOutData(request.getResponse());
    }

    @Subscribe
    public void onEvent(FetchCompleteMeetOutFailure request) {

    }

    @Subscribe
    public void onEvent(BadRequest request) {

    }
}

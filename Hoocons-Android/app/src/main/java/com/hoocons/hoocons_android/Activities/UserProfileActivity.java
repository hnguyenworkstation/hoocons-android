package com.hoocons.hoocons_android.Activities;

import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.TagConstraint;
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
import com.github.ppamorim.dragger.DraggerActivity;
import com.hoocons.hoocons_android.Adapters.UserProfileAndEventAdapter;
import com.hoocons.hoocons_android.CustomUI.view.ViewHelper;
import com.hoocons.hoocons_android.EventBus.FetchEventListSuccessEvBusRequest;
import com.hoocons.hoocons_android.EventBus.FetchUserInfoCompleteEvBusRequest;
import com.hoocons.hoocons_android.EventBus.StartEventChildImages;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Interface.EventAdapterListener;
import com.hoocons.hoocons_android.Interface.InfiniteScrollListener;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.Networking.Responses.UserInfoResponse;
import com.hoocons.hoocons_android.Parcel.EventParcel;
import com.hoocons.hoocons_android.Parcel.MultiImagesEventClickedParcel;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.FetchCreatedEventJob;
import com.hoocons.hoocons_android.Tasks.Jobs.GetSelfInfoJob;
import com.hoocons.hoocons_android.Tasks.Jobs.LikeEventJob;
import com.hoocons.hoocons_android.Tasks.Jobs.UnLikeEventJob;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcel;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class UserProfileActivity extends DraggerActivity
        implements ObservableScrollViewCallbacks, View.OnClickListener,
        EventAdapterListener {
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

    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
    private final String TAG = UserProfileActivity.class.getSimpleName();

    private View mOverlayView;
    private int mActionBarSize;
    private int mFlexibleSpaceImageHeight;
    private boolean isMySelf;
    private Intent mIntent;
    private Handler handler;

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
    private PopupMenu eventPopup;

    private UserProfileAndEventAdapter mEventsAdapter;
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        setContentView(R.layout.activity_user_profile);
        ButterKnife.bind(this);

        mIntent = getIntent();
        isMySelf = mIntent.getBooleanExtra(MYSELF, false);

        handler = new Handler();

        eventResponseList = new ArrayList<>();
        mEventsAdapter = new UserProfileAndEventAdapter(this, eventResponseList, this, isMySelf);

        initGeneralView();

        if (isMySelf) {
            jobManager.addJobInBackground(new GetSelfInfoJob());
            jobManager.addJobInBackground(new FetchCreatedEventJob(eventResponseList.size(),
                    eventResponseList.size() + EVENT_PACK));
        }

        initEventRecyclerView();
    }

    private void initEventRecyclerView() {
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false);
        ((SimpleItemAnimator) mRecyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setAdapter(mEventsAdapter);
        mRecyclerView.addOnScrollListener(new InfiniteScrollListener((LinearLayoutManager) mLayoutManager) {

            @Override
            protected void loadMoreItems() {
                isLoading = true;
                loadMoreEvents();
            }

            @Override
            public int getTotalItems() {
                return mEventsAdapter.getItemCount();
            }

            @Override
            public boolean isLastItem() {
                return ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition()
                        == (mEventsAdapter.getItemCount() - 1);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void loadMoreEvents() {
        mEventsAdapter.addLoadingFooter();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mEventsAdapter.removeLoadingFooter();
                isLoading = false;
            }
        }, 2000);
    }

    private void loadActionBarProfileImage(String url) {
        Glide.with(this)
                .load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .apply(RequestOptions.circleCropTransform())
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
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

        mActionBack.setOnClickListener(this);
        mActionMore.setOnClickListener(this);
        mWallpaperImage.setOnClickListener(this);
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

    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("ChannelActivity", "popping backstack");
            fm.popBackStack();
            finish();
            overridePendingTransition(R.anim.fix_anim, R.anim.slide_down_out);
        } else {
            Log.i("ChannelActivity", "nothing on backstack, calling super");
            super.onBackPressed();
        }
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
        mEventsAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onEvent(StartEventChildImages request) {
        MultiImagesEventClickedParcel parcel = new MultiImagesEventClickedParcel();

        parcel.setResponseList(eventResponseList.get(request.getEventPosition()).getMedias());
        parcel.setClickedPosition( request.getImagePosition());
        parcel.setTextContent(eventResponseList.get(request.getEventPosition()).getTextContent());
        parcel.setUserDisplayName(eventResponseList.get(request.getEventPosition()).getUserInfo().getDisplayName());

        Intent listImages = new Intent(UserProfileActivity.this, FullEventImagesActivity.class);
        listImages.putExtra("event_images_pack", Parcels.wrap(parcel));

        startActivity(listImages);
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_back:
                onBackPressed();
                break;
            case R.id.action_more:
                break;
            default:
                break;
        }
    }

    @Override
    public void onLikeClicked(int position) {
        EventResponse response = eventResponseList.get(position);
        String likeTag = "LIKE-" + String.valueOf(response.getEventId());
        String unlikeTag = "UNLIKE-" + String.valueOf(response.getEventId());

        if (response.getIsLiked()) {
            response.setIsLiked(false);
            response.setLikesCount(response.getLikesCount() - 1);
            try {
                jobManager.cancelJobsInBackground(null, TagConstraint.ANY, likeTag);
            } catch (Exception e) {
                Log.e(TAG, "onLikeClicked: " + e.toString());
            } finally {
                jobManager.addJobInBackground(new UnLikeEventJob(unlikeTag, response.getEventId()));
            }
        } else {
            response.setIsLiked(true);
            response.setLikesCount(response.getLikesCount() + 1);
            jobManager.addJobInBackground(new LikeEventJob(likeTag, response.getEventId()));
        }

        // mEventsAdapter.notifyItemChanged(position + mEventsAdapter.getEXTRA_ITEMS());
        mEventsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onCommentClicked(int position) {
        EventResponse response = eventResponseList.get(position);
        EventParcel parcel = new EventParcel();
        parcel.setId(response.getEventId());
        parcel.setLikeCount(response.getLikesCount());
        parcel.setLiked(response.getIsLiked());

        Intent commentIntent = new Intent(this, CommentEventActivity.class);
        commentIntent.putExtra("event", Parcels.wrap(parcel));
        startActivity(commentIntent);
    }

    @Override
    public void onShareClicked(View view, int position) {

    }

    @Override
    public void onPhotoClicked(int position) {

    }

    @Override
    public void onVideoClicked(int position) {

    }

    @Override
    public void onWebThumbClicked(int position) {

    }

    @Override
    public void onEventImageClicked(int eventPos, int imagePos) {

    }

    @Override
    public void onOptionClicked(View view, final int position) {
        EventResponse response = eventResponseList.get(position);
        eventPopup = new PopupMenu(UserProfileActivity.this, view);

        if (response.getUserInfo().getUser() == SharedPreferencesManager.getDefault().getUserId()) {
            // This event belong to me
            eventPopup.inflate(R.menu.own_event_action);
        }

        eventPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_event:
                        Intent intent = new Intent(UserProfileActivity.this, NewEventActivity.class);
                        if (eventResponseList.get(position).getContainEvent() != null) {
                            intent.putExtra("shared_event",
                                    Parcels.wrap(AppUtils.getEventParcel(eventResponseList
                                            .get(position).getContainEvent())));
                        } else {
                            intent.putExtra("shared_event",
                                    Parcels.wrap(AppUtils.getEventParcel(eventResponseList
                                            .get(position))));
                        }
                        startActivity(intent);
                        return true;
                    case R.id.delete_event:
                        return true;
                    default:
                        return false;
                }
            }
        });

        eventPopup.show();
    }
}

package com.hoocons.hoocons_android.ViewFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.birbit.android.jobqueue.JobManager;
import com.birbit.android.jobqueue.TagConstraint;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hoocons.hoocons_android.Activities.AddCombinationActivity;
import com.hoocons.hoocons_android.Activities.AroundActivity;
import com.hoocons.hoocons_android.Activities.CommentEventActivity;
import com.hoocons.hoocons_android.Activities.FullEventImagesActivity;
import com.hoocons.hoocons_android.Activities.NewEventActivity;
import com.hoocons.hoocons_android.Activities.UserProfileActivity;
import com.hoocons.hoocons_android.Adapters.FeaturedEventsAdapter;
import com.hoocons.hoocons_android.CustomUI.DividerItemDecoration;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.FetchFeaturedActivitySuccess;
import com.hoocons.hoocons_android.EventBus.ServerErrorRequest;
import com.hoocons.hoocons_android.EventBus.StartEventChildImages;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Helpers.PermissionUtils;
import com.hoocons.hoocons_android.Helpers.UserUtils;
import com.hoocons.hoocons_android.Interface.EventAdapterListener;
import com.hoocons.hoocons_android.Interface.InfiniteScrollListener;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.Responses.ActivityResponse;
import com.hoocons.hoocons_android.Networking.Responses.EventResponse;
import com.hoocons.hoocons_android.Networking.Responses.SemiUserInfoResponse;
import com.hoocons.hoocons_android.Parcel.EventParcel;
import com.hoocons.hoocons_android.Parcel.MultiImagesEventClickedParcel;
import com.hoocons.hoocons_android.Parcel.UserParcel;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.FetchFeaturedActivityJob;
import com.hoocons.hoocons_android.Tasks.Jobs.LikeEventJob;
import com.hoocons.hoocons_android.Tasks.Jobs.UnLikeEventJob;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FeaturedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener,
        View.OnClickListener, EventAdapterListener {
    @BindView(R.id.screen_no_connection_error)
    RelativeLayout mErrorScreen;
    @BindView(R.id.screen_featured_fragment)
    RelativeLayout mMainScreen;
    @BindView(R.id.swipe_ref_layout)
    SwipeRefreshLayout mSwipeRefLayout;
    @BindView(R.id.header_profile)
    ImageView mImageHeader;

    @BindView(R.id.featured_recycler)
    RecyclerView mRecycler;

    @BindView(R.id.left_action)
    ImageButton mLeftAction;
    @BindView(R.id.right_action)
    ImageButton mRightAction;

    private List<ActivityResponse> activityResponses;
    private final int MAX_ACTIVITIES_PER_PAGE = 20;
    private final String TAG = FeaturedFragment.class.getSimpleName();
    private boolean isLoading = false;
    private boolean canLoadMore = false;
    private FeaturedEventsAdapter mAdapter;
    private PopupMenu eventPopup;
    private Handler handler;

    private DividerItemDecoration spaceDecoration;
    private int currentPage = 1;

    private final String MYSELF = "IS_MY_SELF";
    private static final int LOCATION_PERMISSION_REQUEST = 1;

    public FeaturedFragment() {
    }

    public static FeaturedFragment newInstance(String param1, String param2) {
        FeaturedFragment fragment = new FeaturedFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);

        if (getArguments() != null) {

        }
        handler = new Handler();
        activityResponses = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_featured, container, false);
        ButterKnife.bind(this, rootView);

        mAdapter = new FeaturedEventsAdapter(getActivity(), getContext(), activityResponses, this);

        initView();
        initOnClickListener();

        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Run first init when created
        mSwipeRefLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        BaseApplication.getInstance().getJobManager()
                                .addJobInBackground(new FetchFeaturedActivityJob(1));
                        initRecyclerView();
                    }
                }
        );
    }

    private void initOnClickListener() {
        mImageHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserProfileActivity.class)
                        .putExtra("user_info", Parcels.wrap(UserUtils.getSelfParcel())));
            }
        });

        mRightAction.setOnClickListener(this);
        mLeftAction.setOnClickListener(this);
    }

    private void initView() {
        mSwipeRefLayout.setOnRefreshListener(this);

        loadImageHeader();
    }

    private void loadImageHeader() {
        BaseApplication.getInstance().getGlide()
                .load(SharedPreferencesManager.getDefault().getUserProfileUrl())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.errorOf(R.drawable.image_holder))
                .into(mImageHeader);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.right_action:
                startNewCombinationActivity();
                break;
            case R.id.left_action:
                startNearMeActivity();
                break;
            default:
                break;
        }
    }

    private void initRecyclerView() {
        activityResponses.clear();

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        RecyclerView.ItemAnimator animator = mRecycler.getItemAnimator();
        if (animator instanceof SimpleItemAnimator) {
            ((SimpleItemAnimator) animator).setSupportsChangeAnimations(false);
        }
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.setFocusable(false);
        mRecycler.setAdapter(mAdapter);

        if (spaceDecoration != null) {
            mRecycler.removeItemDecoration(spaceDecoration);
        }

        spaceDecoration = new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST);

        mRecycler.addItemDecoration(spaceDecoration);
        mRecycler.addOnScrollListener(new InfiniteScrollListener((LinearLayoutManager) mLayoutManager) {

            @Override
            protected void loadMoreItems() {
                // Load more activity here
                if (canLoadMore) {
                    loadMoreActivities();
                }
            }

            @Override
            public int getTotalItems() {
                return activityResponses.size();
            }

            @Override
            public boolean isLastItem() {
                return ((LinearLayoutManager) mLayoutManager).findLastCompletelyVisibleItemPosition()
                        == (activityResponses.size() -1);
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }
        });
    }

    private void loadMoreActivities() {
        mAdapter.addLoadingFooter();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mAdapter.removeLoadingFooter();
                isLoading = false;
            }
        }, 2000);
    }

    @Subscribe
    public void onEvent(FetchFeaturedActivitySuccess ev) {
        activityResponses.addAll(ev.getActivityResponse().getResults());
        canLoadMore = ev.getActivityResponse().getNext() != null;

        currentPage++;

        mSwipeRefLayout.setRefreshing(false);
        mAdapter.notifyDataSetChanged();
    }

    @Subscribe
    public void onEvent(BadRequest ev) {
        mSwipeRefLayout.setRefreshing(false);
    }

    @Subscribe
    public void onEvent(ServerErrorRequest ev) {
        mSwipeRefLayout.setRefreshing(false);
    }

    @Subscribe
    public void onEvent(StartEventChildImages request) {
        MultiImagesEventClickedParcel parcel = new MultiImagesEventClickedParcel();

        if (activityResponses.get(request.getEventPosition()).getTarget().getContainEvent() == null) {
            parcel.setResponseList(activityResponses
                    .get(request.getEventPosition())
                    .getTarget()
                    .getMedias());
            parcel.setClickedPosition( request.getImagePosition());
            parcel.setTextContent(activityResponses
                    .get(request.getEventPosition())
                    .getTarget()
                    .getTextContent());
            parcel.setUserDisplayName(activityResponses
                    .get(request.getEventPosition())
                    .getTarget()
                    .getAuthor()
                    .getDisplayName());
        } else {
            parcel.setResponseList(activityResponses
                    .get(request.getEventPosition())
                    .getTarget()
                    .getContainEvent().getMedias());
            parcel.setClickedPosition(request.getImagePosition());
            parcel.setTextContent(activityResponses
                    .get(request.getEventPosition())
                    .getTarget()
                    .getContainEvent()
                    .getTextContent());
            parcel.setUserDisplayName(activityResponses
                    .get(request.getEventPosition())
                    .getTarget()
                    .getContainEvent()
                    .getAuthor().getDisplayName());
        }

        Intent listImages = new Intent(getActivity(), FullEventImagesActivity.class);
        listImages.putExtra("event_images_pack", Parcels.wrap(parcel));

        startActivity(listImages);
    }


    private boolean mayNeedLocationPermission() {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

        return PermissionUtils.requestPermissions(getActivity(), LOCATION_PERMISSION_REQUEST, permissions);
    }

    private void startNearMeActivity() {
        startActivity(new Intent(getActivity(), AroundActivity.class));
        getActivity().overridePendingTransition(R.anim.slide_bottom_up, R.anim.fix_anim);
    }

    private void startNewCombinationActivity() {
        startActivity(new Intent(getActivity(), AddCombinationActivity.class));
    }


    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST:
                Log.i(TAG, "Received response for Location permission request.");

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.i(TAG, "LOCATION permission has now been granted. Showing preview.");
                    startNearMeActivity();
                } else {
                    Log.i(TAG, "LOCATION permission was NOT granted.");
                }
                return;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onEventHeaderClicked(int position) {

    }

    @Override
    public void onUserInfoClicked(int position) {
        SemiUserInfoResponse response = activityResponses.get(position).getActor();
        UserParcel parcel = UserUtils.getUserParcelFromSemiResponse(response);

        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
        intent.putExtra("user_info", Parcels.wrap(parcel));
        startActivity(intent);
    }

    @Override
    public void onSharedUserInfoClicked(int position) {

    }

    @Override
    public void onLikeClicked(int position) {
        JobManager jobManager = BaseApplication.getInstance().getJobManager();
        EventResponse response = activityResponses.get(position).getTarget();
        String likeTag = "LIKE-" + String.valueOf(response.getId());
        String unlikeTag = "UNLIKE-" + String.valueOf(response.getId());

        if (response.isLiked()) {
            response.setLiked(false);
            response.setLikesCount(response.getLikesCount() - 1);
            try {
                jobManager.cancelJobsInBackground(null, TagConstraint.ANY, likeTag);
            } catch (Exception e) {
                Log.e(TAG, "onLikeClicked: " + e.toString());
            } finally {
                jobManager.addJobInBackground(new UnLikeEventJob(unlikeTag, response.getId()));
            }
        } else {
            response.setLiked(true);
            response.setLikesCount(response.getLikesCount() + 1);
            jobManager.addJobInBackground(new LikeEventJob(likeTag, response.getId()));
        }

        // mEventsAdapter.notifyItemChanged(position + mEventsAdapter.getEXTRA_ITEMS());
        mAdapter.notifyItemChanged(position);
    }

    @Override
    public void onCommentClicked(int position) {
        EventResponse response = activityResponses.get(position).getTarget();
        EventParcel parcel = new EventParcel();
        parcel.setId(response.getId());
        parcel.setLikeCount(response.getLikesCount());
        parcel.setLiked(response.isLiked());

        Intent commentIntent = new Intent(getActivity(), CommentEventActivity.class);
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
        EventResponse response = activityResponses.get(position).getTarget();
        eventPopup = new PopupMenu(getActivity(), view);

        if (response.getAuthor().getUser() == SharedPreferencesManager.getDefault().getUserId()) {
            // This event belong to me
            eventPopup.inflate(R.menu.own_event_action);
        }

        eventPopup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.share_event:
                        Intent intent = new Intent(getActivity(), NewEventActivity.class);
                        if (activityResponses.get(position).getTarget().getContainEvent() != null) {
                            intent.putExtra("shared_event",
                                    Parcels.wrap(AppUtils.getEventParcel(activityResponses
                                            .get(position).getTarget().getContainEvent())));
                        } else {
                            intent.putExtra("shared_event",
                                    Parcels.wrap(AppUtils.getEventParcel(activityResponses
                                            .get(position).getTarget())));
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

    @Override
    public void onQuickCommentClicked(int position) {
        Toast.makeText(getContext(), "Comment " + String.valueOf(position), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBottomProfileClicked(int position) {

    }

    @Override
    public void onRefresh() {
        currentPage = 0;
        BaseApplication.getInstance().getJobManager().addJobInBackground(new FetchFeaturedActivityJob(1));
        initRecyclerView();
    }
}

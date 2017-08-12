package com.hoocons.hoocons_android.ViewFragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hoocons.hoocons_android.Activities.AddCombinationActivity;
import com.hoocons.hoocons_android.Activities.AroundActivity;
import com.hoocons.hoocons_android.Activities.NewEventActivity;
import com.hoocons.hoocons_android.Activities.UserProfileActivity;
import com.hoocons.hoocons_android.Adapters.FeaturedEventsAdapter;
import com.hoocons.hoocons_android.EventBus.BadRequest;
import com.hoocons.hoocons_android.EventBus.FetchFeaturedActivitySuccess;
import com.hoocons.hoocons_android.EventBus.ServerErrorRequest;
import com.hoocons.hoocons_android.Helpers.PermissionUtils;
import com.hoocons.hoocons_android.Interface.EventAdapterListener;
import com.hoocons.hoocons_android.Interface.InfiniteScrollListener;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Managers.SharedPreferencesManager;
import com.hoocons.hoocons_android.Networking.Responses.ActivityResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.FetchFeaturedActivityJob;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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
    @BindView(R.id.bottom_menu)
    RelativeLayout mBottomMenuLayout;
    @BindView(R.id.header_profile)
    ImageView mImageHeader;
    @BindView(R.id.action_near_me)
    ImageButton mNearMeBtn;
    @BindView(R.id.action_add)
    ImageButton mAddBtn;

    @BindView(R.id.featured_recycler)
    RecyclerView mRecycler;

    private List<ActivityResponse> activityResponses;
    private final int LOCATION_PERMISSION_REQUEST = 1;
    private final String TAG = FeaturedFragment.class.getSimpleName();
    private boolean isLoading = false;
    private FeaturedEventsAdapter mAdapter;

    private final String MYSELF = "IS_MY_SELF";

    public FeaturedFragment() {
        // Required empty public constructor
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

        activityResponses = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_featured, container, false);
        ButterKnife.bind(this, rootView);

        mAdapter = new FeaturedEventsAdapter(getContext(), activityResponses, this);

        initView();
        initOnClickListener();

        return rootView;
    }

    private void initOnClickListener() {
        mImageHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), UserProfileActivity.class)
                        .addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                        .putExtra(MYSELF, true));
            }
        });

        mAddBtn.setOnClickListener(this);
        mNearMeBtn.setOnClickListener(this);
    }

    private void initView() {
        mSwipeRefLayout.setOnRefreshListener(this);

        loadImageHeader();
    }

    private void loadImageHeader() {
        Glide.with(this)
                .load(SharedPreferencesManager.getDefault().getUserProfileUrl())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .apply(RequestOptions.noAnimation())
                .apply(RequestOptions.circleCropTransform())
                .apply(RequestOptions.errorOf(R.drawable.image_holder))
                .into(mImageHeader);
    }

    private void startNewCombinationActivity() {
        startActivity(new Intent(getActivity(), AddCombinationActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
    }

    private void startNearMeActivity() {
        startActivity(new Intent(getActivity(), AroundActivity.class)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        getActivity().overridePendingTransition(R.anim.slide_bottom_up, R.anim.fix_anim);
    }

    private boolean mayNeedLocationPermission() {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);

        return PermissionUtils.requestPermissions(getActivity(), LOCATION_PERMISSION_REQUEST, permissions);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_add:
                startNewCombinationActivity();
                break;
            case R.id.action_near_me:
                if (mayNeedLocationPermission()) {
                    startNearMeActivity();
                }
                break;
            default:
                break;
        }
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

    private void initRecyclerView() {
        activityResponses.clear();

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRecycler.setLayoutManager(mLayoutManager);
        mRecycler.setItemAnimator(new DefaultItemAnimator());
        mRecycler.setNestedScrollingEnabled(false);
        mRecycler.setAdapter(mAdapter);
        mRecycler.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        mRecycler.addOnScrollListener(new InfiniteScrollListener((LinearLayoutManager) mLayoutManager) {

            @Override
            protected void loadMoreItems() {
                isLoading = true;
                // Load more activity here
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

    @Subscribe
    public void onEvent(FetchFeaturedActivitySuccess ev) {
        activityResponses.addAll(ev.getActivityResponses());
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

    @Override
    public void onLikeClicked(int position) {

    }

    @Override
    public void onCommentClicked(int position) {

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
    public void onOptionClicked(View view, int position) {

    }

    @Override
    public void onRefresh() {
        BaseApplication.getInstance().getJobManager().addJobInBackground(new FetchFeaturedActivityJob());
        initRecyclerView();
    }

    @Override
    public void onStart() {
        mSwipeRefLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        BaseApplication.getInstance().getJobManager()
                                .addJobInBackground(new FetchFeaturedActivityJob());
                        initRecyclerView();
                    }
                }
        );
        super.onStart();
    }
}

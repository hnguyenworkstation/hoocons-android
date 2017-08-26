package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SimpleItemAnimator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.hoocons.hoocons_android.Adapters.FriendConnectionsAdapter;
import com.hoocons.hoocons_android.Adapters.FriendRequestAdapter;
import com.hoocons.hoocons_android.EventBus.FetchFriendRequestComplete;
import com.hoocons.hoocons_android.EventBus.FetchRelationshipComplete;
import com.hoocons.hoocons_android.Interface.OnFriendConnectionAdapterListener;
import com.hoocons.hoocons_android.Interface.OnFriendRequestAdapterListener;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Networking.ApiViewSets.FriendshipRequestApiViewSet;
import com.hoocons.hoocons_android.Networking.Responses.FriendshipRequestResponse;
import com.hoocons.hoocons_android.Networking.Responses.RelationshipResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.FetchFriendConnectionsJob;
import com.hoocons.hoocons_android.Tasks.Jobs.FetchSemiFriendRequestJob;
import com.vstechlab.easyfonts.EasyFonts;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectionsFragment extends Fragment implements
        OnFriendRequestAdapterListener, OnFriendConnectionAdapterListener {
    @BindView(R.id.swipe_ref)
    SwipeRefreshLayout mSwipeRef;

    @BindView(R.id.friend_request_layout)
    LinearLayout mFriendRequestLayout;

    @BindView(R.id.new_friendrequest_tv)
    TextView mNewRequestTv;

    @BindView(R.id.fr_request_recycler)
    RecyclerView mFriendRequestRecycler;

    @BindView(R.id.friend_list_layout)
    LinearLayout mFriendsLayout;

    @BindView(R.id.friend_list_tv)
    TextView mFriendTv;

    @BindView(R.id.friends_recycler)
    RecyclerView mFriendsRecycler;

    @BindView(R.id.view_more_request)
    LinearLayout mViewAllRequestsBtn;

    @BindView(R.id.btn_tv)
    TextView mViewMoreTextView;

    @BindView(R.id.friend_request_content)
    LinearLayout mFriendRequestContent;

    /* EMPTY SCREEN FIELDS */

    @BindView(R.id.empty_connections_view)
    RelativeLayout mEmptyConnectionsView;
    @BindView(R.id.error_image)
    ImageView mErrorImage;
    @BindView(R.id.title)
    TextView mErrorTitle;
    @BindView(R.id.message)
    TextView mErrorMessage;
    @BindView(R.id.invite_friend_btn)
    LinearLayout mInviteButton;

    private FriendshipRequestApiViewSet friendshipRequestApiViewSet;
    private FriendRequestAdapter friendRequestAdapter;
    private FriendConnectionsAdapter friendConnectionsAdapter;
    private List<FriendshipRequestResponse> requestResponseList;
    private List<RelationshipResponse> friendRelationshipList;

    private boolean fetchRequestCompleted = false;
    private boolean fetchFriendsCompleted = false;

    public ConnectionsFragment() {
        // Required empty public constructor
    }

    public static ConnectionsFragment newInstance(String param1, String param2) {
        ConnectionsFragment fragment = new ConnectionsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_connections, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        // Inflate the layout for this fragment
        BaseApplication.getInstance().getJobManager()
                .addJobInBackground(new FetchSemiFriendRequestJob());

        BaseApplication.getInstance().getJobManager()
                .addJobInBackground(new FetchFriendConnectionsJob());

        requestResponseList = new ArrayList<>();
        friendRelationshipList = new ArrayList<>();

        initRequestView();
        initFriendListView();
    }

    private void initRequestView() {
        friendRequestAdapter = new FriendRequestAdapter(getContext(), requestResponseList, this);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mFriendRequestRecycler.setFocusable(false);
        mFriendRequestRecycler.setLayoutManager(mLayoutManager);
        mFriendRequestRecycler.setHasFixedSize(false);
        mFriendRequestRecycler.setAdapter(friendRequestAdapter);
    }

    private void initFriendListView() {
        friendConnectionsAdapter = new FriendConnectionsAdapter(getContext(), friendRelationshipList, this);

        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mFriendsRecycler.setFocusable(false);
        mFriendsRecycler.setLayoutManager(mLayoutManager);
        mFriendsRecycler.setHasFixedSize(false);
        mFriendsRecycler.setAdapter(friendConnectionsAdapter);
    }

    private void showEmptyConnectionView() {
        mEmptyConnectionsView.setVisibility(View.VISIBLE);
        mErrorTitle.setTypeface(EasyFonts.robotoBold(getContext()));
        mErrorMessage.setTypeface(EasyFonts.robotoRegular(getContext()));
        BaseApplication.getInstance().getGlide()
                .load(R.drawable.image_empty_relationship)
                .apply(RequestOptions.centerInsideTransform())
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .into(mErrorImage);
    }

    private void initCompleteRequestView() {
        if (fetchRequestCompleted && friendshipRequestApiViewSet.getCount() == 0) {
            mFriendRequestLayout.setVisibility(View.GONE);
        }

        if (fetchFriendsCompleted && friendRelationshipList.size() == 0) {
            mFriendsLayout.setVisibility(View.GONE);
        }

        if (fetchRequestCompleted && fetchFriendsCompleted &&
                requestResponseList.size() == 0 &&
                friendRelationshipList.size() == 0) {
            showEmptyConnectionView();
        } else {
            BaseApplication.getInstance().getGlide().clear(mErrorImage);
            mEmptyConnectionsView.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onEvent(FetchFriendRequestComplete requestComplete) {
        fetchRequestCompleted = true;
        friendshipRequestApiViewSet = requestComplete.getFriendshipRequestApiViewSet();
        requestResponseList = requestComplete.getFriendshipRequestApiViewSet().getResults();
        friendRequestAdapter.notifyDataSetChanged();
        initCompleteRequestView();
    }

    @Subscribe
    public void onEvent(FetchRelationshipComplete relationshipComplete) {
        fetchFriendsCompleted = true;
        friendRelationshipList = relationshipComplete.getRelationshipResponseList();
        friendConnectionsAdapter.notifyDataSetChanged();
        initCompleteRequestView();
    }
}

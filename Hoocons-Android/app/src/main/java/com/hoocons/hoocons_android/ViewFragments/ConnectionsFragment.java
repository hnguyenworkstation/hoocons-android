package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ConnectionsFragment extends Fragment {
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

    @BindView(R.id.empty_connections_view)
    RelativeLayout mEmptyConnectionsView;

    @BindView(R.id.view_more_request)
    LinearLayout mViewAllRequestsBtn;

    @BindView(R.id.btn_tv)
    TextView mViewMoreTextView;

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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_connections, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }
}

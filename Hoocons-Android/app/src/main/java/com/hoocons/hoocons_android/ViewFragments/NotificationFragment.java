package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NotificationFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.screen_null_notification)
    RelativeLayout mEmptyNotifScreen;
    @BindView(R.id.screen_notification)
    RelativeLayout mNotifiScreen;
    @BindView(R.id.notification_swipe_ref)
    SwipeRefreshLayout mSwipeRefLayout;
    @BindView(R.id.notification_recycler)
    RecyclerView mNotificationRecycler;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    public NotificationFragment() {
        // Required empty public constructor
    }

    public static NotificationFragment newInstance(String param1, String param2) {
        NotificationFragment fragment = new NotificationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_notification, container, false);
        ButterKnife.bind(this, rootView);

        mSwipeRefLayout.setOnRefreshListener(this);
        mSwipeRefLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Todo: Add function to load fetch notification on the server
            }
        }, 2000);

        return rootView;
    }

    @Override
    public void onRefresh() {

    }
}

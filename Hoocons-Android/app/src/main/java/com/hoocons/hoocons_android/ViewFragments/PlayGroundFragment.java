package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Adapters.ChannelCardViewAdapter;
import com.hoocons.hoocons_android.Adapters.DiscoverTopPanelAdapter;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayGroundFragment extends Fragment {
    @BindView(R.id.swipe_ref)
    SwipeRefreshLayout mSwipeRefLayout;
    @BindView(R.id.channel_recycler)
    RecyclerView mChannelRecycler;


    private ChannelCardViewAdapter channelCardViewAdapter;

    public PlayGroundFragment() {
        // Required empty public constructor
    }

    public static PlayGroundFragment newInstance(String param1, String param2) {
        PlayGroundFragment fragment = new PlayGroundFragment();
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
        return inflater.inflate(R.layout.fragment_play_ground, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initTopPanelRecycler();
    }

    private void initTopPanelRecycler() {
        SnapHelper snapHelper = new LinearSnapHelper();

        channelCardViewAdapter = new ChannelCardViewAdapter();
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.HORIZONTAL, false);
        mChannelRecycler.setLayoutManager(mLayoutManager);
        mChannelRecycler.setItemAnimator(new DefaultItemAnimator());
        mChannelRecycler.setAdapter(channelCardViewAdapter);

        snapHelper.attachToRecyclerView(mChannelRecycler);
    }

}

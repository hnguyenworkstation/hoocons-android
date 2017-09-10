package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.hoocons.hoocons_android.EventBus.FetchOwnedChannelsComplete;
import com.hoocons.hoocons_android.Networking.ApiViewSets.ChannelApiViewSet;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlayGroundChannelFragment extends Fragment {
    @BindView(R.id.swipe_ref)
    SwipeRefreshLayout mSwipeRefLayout;
    @BindView(R.id.nested_scroll)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private boolean isFirstTime = true;
    private ChannelApiViewSet channelApiViewSet;

    public PlayGroundChannelFragment() {
        // Required empty public constructor
    }

    public static PlayGroundChannelFragment newInstance() {
        PlayGroundChannelFragment fragment = new PlayGroundChannelFragment();
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
        return inflater.inflate(R.layout.fragment_play_ground_channel, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    private void initView() {

    }

    public void onRestore() {
        if (isFirstTime) {
            initView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    /*
    *   EVENT CATCHING
    * */
    @Subscribe
    public void onEvent(FetchOwnedChannelsComplete fetchOwnedChannelsComplete) {
        channelApiViewSet = fetchOwnedChannelsComplete.getChannelApiViewSet();
    }
}

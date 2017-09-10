package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.hoocons.hoocons_android.Adapters.ChannelLargeCardViewAdapter;
import com.hoocons.hoocons_android.Adapters.EventCardViewAdapter;
import com.hoocons.hoocons_android.CustomUI.DividerItemDecoration;
import com.hoocons.hoocons_android.CustomUI.StaggeredItemDecorator;
import com.hoocons.hoocons_android.EventBus.FetchOwnedChannelsComplete;
import com.hoocons.hoocons_android.Interface.OnChannelProfileClickListener;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Networking.ApiViewSets.ChannelApiViewSet;
import com.hoocons.hoocons_android.Networking.Responses.ChannelProfileResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.FetchOwnedChannelJob;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlayGroundChannelFragment extends Fragment implements OnChannelProfileClickListener {
    @BindView(R.id.swipe_ref)
    SwipeRefreshLayout mSwipeRefLayout;
    @BindView(R.id.nested_scroll)
    NestedScrollView mNestedScrollView;
    @BindView(R.id.recycler)
    RecyclerView recyclerView;
    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    @BindView(R.id.content_view)
    RelativeLayout mContentView;

    private boolean isFirstTime = true;
    private ChannelApiViewSet channelApiViewSet;
    private ChannelLargeCardViewAdapter mAdapter;
    private DividerItemDecoration spaceDecoration;
    private List<ChannelProfileResponse> responseList;

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
        responseList = new ArrayList<>();
        BaseApplication.getInstance().getJobManager().addJobInBackground(
                new FetchOwnedChannelJob()
        );
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

        initView();
    }

    public void onRestore() {
        if (isFirstTime) {
            runInitViewInBackground();
        }
    }

    private void runInitViewInBackground() {
        new Thread() {
            @Override
            public void run() {
                try {
                    synchronized (this) {
                        wait(500);

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mAdapter != null) {
                                    mAdapter.notifyDataSetChanged();
                                }

                                mProgressBar.setVisibility(View.GONE);
                                mContentView.setVisibility(View.VISIBLE);
                            }
                        });

                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                isFirstTime = false;
            };
        }.start();
    }

    private void initView() {
        mAdapter = new ChannelLargeCardViewAdapter(getContext(), responseList, this);
        final RecyclerView.LayoutManager mLayoutManager =
                new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        if (spaceDecoration != null) {
            recyclerView.removeItemDecoration(spaceDecoration);
        } else {
            spaceDecoration = new DividerItemDecoration(getContext(),
                    DividerItemDecoration.VERTICAL_LIST);
        }
        recyclerView.addItemDecoration(spaceDecoration);
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
        responseList = channelApiViewSet.getResults();
    }
}

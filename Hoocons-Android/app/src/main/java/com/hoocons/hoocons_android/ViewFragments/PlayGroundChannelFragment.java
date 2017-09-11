package com.hoocons.hoocons_android.ViewFragments;


import android.content.Intent;
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
import android.widget.Toast;

import com.hoocons.hoocons_android.Activities.ChannelActivity;
import com.hoocons.hoocons_android.Adapters.ChannelLargeCardViewAdapter;
import com.hoocons.hoocons_android.Adapters.EventCardViewAdapter;
import com.hoocons.hoocons_android.CustomUI.DividerItemDecoration;
import com.hoocons.hoocons_android.CustomUI.StaggeredItemDecorator;
import com.hoocons.hoocons_android.EventBus.FetchOwnedChannelsComplete;
import com.hoocons.hoocons_android.Helpers.AppUtils;
import com.hoocons.hoocons_android.Interface.OnChannelProfileClickListener;
import com.hoocons.hoocons_android.Managers.BaseApplication;
import com.hoocons.hoocons_android.Networking.ApiViewSets.ChannelApiViewSet;
import com.hoocons.hoocons_android.Networking.Responses.ChannelProfileResponse;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.Tasks.Jobs.FetchOwnedChannelJob;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class PlayGroundChannelFragment extends Fragment implements OnChannelProfileClickListener {
    @BindView(R.id.swipe_ref)
    SwipeRefreshLayout mSwipeRefLayout;

    @BindView(R.id.nested_scroll)
    NestedScrollView mNestedScrollView;

    @BindView(R.id.channel_recycler)
    RecyclerView recyclerView;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    private boolean isFirstTime = true;
    private ChannelApiViewSet channelApiViewSet;
    private ChannelLargeCardViewAdapter mAdapter;
    private DividerItemDecoration spaceDecoration;
    private List<ChannelProfileResponse> channelProfileResponseList;

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
        channelProfileResponseList = new ArrayList<>();
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
                                mAdapter.notifyDataSetChanged();
                                mProgressBar.setVisibility(View.GONE);
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
        channelProfileResponseList.clear();
        mAdapter = new ChannelLargeCardViewAdapter(getContext(), channelProfileResponseList, this);

        final RecyclerView.LayoutManager mLayoutManager =
                new GridLayoutManager(getContext(), 2, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(mAdapter);
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

    @Override
    public void onChannelProfileClicked(int position) {
        ChannelProfileResponse response = channelProfileResponseList.get(position);
        Intent intent = new Intent(getActivity(), ChannelActivity.class);
        intent.putExtra("channel_profile",
                Parcels.wrap(AppUtils.getChannelProfileParcelFromResponse(response)));
        startActivity(intent);
    }

    @Override
    public void onChannelProfileLongClicked(int position) {
        Toast.makeText(getContext(), "Long clicked: " + String.valueOf(position),
                Toast.LENGTH_SHORT).show();
    }

    /*****************************
    *   EVENT CATCHING
    * *****************************/
    @Subscribe
    public void onEvent(FetchOwnedChannelsComplete fetchOwnedChannelsComplete) {
        channelApiViewSet = fetchOwnedChannelsComplete.getChannelApiViewSet();
        channelProfileResponseList.addAll(channelApiViewSet.getResults());
    }

}

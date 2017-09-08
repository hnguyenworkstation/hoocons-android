package com.hoocons.hoocons_android.ViewFragments;


import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.hoocons.hoocons_android.Adapters.ChannelCardViewAdapter;
import com.hoocons.hoocons_android.Adapters.DiscoverTopPanelAdapter;
import com.hoocons.hoocons_android.CustomUI.RippleAnimationLayout;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayGroundFragment extends Fragment {
    @BindView(R.id.swipe_ref)
    SwipeRefreshLayout mSwipeRefLayout;
    @BindView(R.id.playground_viewpager)
    ViewPager mPlaygroundViewPager;
    @BindView(R.id.search_ripple_anim)
    RippleAnimationLayout mRippleAnimLayout;
    @BindView(R.id.search_around_view)
    RelativeLayout mSearchAroundView;

    private boolean isFirstTime = true;

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
    }

    private void runRippleAnimation() {
        startAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                stopAnimation();
            }
        }, 4000);
    }

    public void onRestore() {
        if (isFirstTime) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized (this) {
                            wait(500);

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    runRippleAnimation();
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
    }

    private void startAnimation() {
        if (!mRippleAnimLayout.isRippleAnimationRunning()) {
            mRippleAnimLayout.startRippleAnimation();
        }
    }

    private void stopAnimation() {
        if (mRippleAnimLayout.isRippleAnimationRunning()) {
            mRippleAnimLayout.stopRippleAnimation();
        }

        mSearchAroundView.setVisibility(View.GONE);
        mSwipeRefLayout.setVisibility(View.VISIBLE);
        mRippleAnimLayout.destroyDrawingCache();
        mSearchAroundView.destroyDrawingCache();
    }


}

package com.hoocons.hoocons_android.ViewFragments;


import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hoocons.hoocons_android.Activities.HooconsMapActivity;
import com.hoocons.hoocons_android.Adapters.ChannelCardViewAdapter;
import com.hoocons.hoocons_android.Adapters.CommunicateViewPagerAdapter;
import com.hoocons.hoocons_android.Adapters.DiscoverTopPanelAdapter;
import com.hoocons.hoocons_android.CustomUI.RippleAnimationLayout;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayGroundFragment extends Fragment {
    @BindView(R.id.playground_viewpager)
    ViewPager mPlaygroundViewPager;

    @BindView(R.id.search_ripple_anim)
    RippleAnimationLayout mRippleAnimLayout;

    @BindView(R.id.search_around_view)
    RelativeLayout mSearchAroundView;

    @BindView(R.id.location_bar)
    RelativeLayout mLocationBar;

    @BindView(R.id.current_address)
    TextView mAddressBarText;

    @BindView(R.id.tabbar)
    TabLayout mTabBar;

    private CommunicateViewPagerAdapter adapter;
    private PlayGroundAllFragment playGroundAllFragment;
    private PlayGroundPeopleFragment playGroundPeopleFragment;
    private PlayGroundChannelFragment playGroundChannelFragment;

    private boolean isFirstTime = true;

    public PlayGroundFragment() {

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

        mLocationBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), HooconsMapActivity.class));
            }
        });
    }

    private void runRippleAnimation() {
        startAnimation();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setupViewPager(mPlaygroundViewPager);
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
        mPlaygroundViewPager.setVisibility(View.VISIBLE);
        mRippleAnimLayout.destroyDrawingCache();
        mSearchAroundView.destroyDrawingCache();
    }

    private void setupViewPager(ViewPager viewPager) {
        if (playGroundAllFragment == null)
            playGroundAllFragment= PlayGroundAllFragment.newInstance("1", "1");

        if (playGroundPeopleFragment == null)
            playGroundPeopleFragment = PlayGroundPeopleFragment.newInstance("1", "1");

        if (playGroundChannelFragment == null)
            playGroundChannelFragment = PlayGroundChannelFragment.newInstance("1", "1");

        if (adapter == null) {
            adapter = new CommunicateViewPagerAdapter(getChildFragmentManager());
            adapter.addFragment(playGroundAllFragment, getResources().getString(R.string.all));
            adapter.addFragment(playGroundPeopleFragment, getResources().getString(R.string.people));
            adapter.addFragment(playGroundChannelFragment, getResources().getString(R.string.channel));
        }

        /** the ViewPager requires a minimum of 1 as OffscreenPageLimit */
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(limit);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Log.e("ASD", "onPageSelected: " + String.valueOf(position));
                if (position == 2) {
                    playGroundChannelFragment.onRestore();
                } else if (position == 0) {
                    playGroundAllFragment.onRestore();
                } else if (position == 1) {
                    playGroundPeopleFragment.onRestore();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mTabBar.setupWithViewPager(viewPager);
        mTabBar.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

}

package com.hoocons.hoocons_android.ViewFragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hoocons.hoocons_android.Adapters.CommunicateViewPagerAdapter;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CommunicationFragment extends Fragment {
    @BindView(R.id.comm_tabbar)
    TabLayout mTabBar;
    @BindView(R.id.comm_viewpager)
    ViewPager mViewPager;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private CommunicateViewPagerAdapter adapter;


    public CommunicationFragment() {
        // Required empty public constructor
    }

    public static CommunicationFragment newInstance(String param1, String param2) {
        CommunicationFragment fragment = new CommunicationFragment();
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
        return inflater.inflate(R.layout.fragment_communication, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        setupViewPager(mViewPager);
    }

    private void setupViewPager(ViewPager viewPager) {
        if (adapter == null) {
            adapter = new CommunicateViewPagerAdapter(getChildFragmentManager());
            adapter.addFragment(MessagingFragment.newInstance("1", "1"), getResources().getString(R.string.messaging));
            adapter.addFragment(InboxFragment.newInstance("1", "1"), getResources().getString(R.string.inbox));
            adapter.addFragment(ConnectionsFragment.newInstance("1", "1"), getResources().getString(R.string.connection));
        }

        /** the ViewPager requires a minimum of 1 as OffscreenPageLimit */
        int limit = (adapter.getCount() > 1 ? adapter.getCount() - 1 : 1);

        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(limit);
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

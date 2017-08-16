package com.hoocons.hoocons_android.Adapters;

/**
 * Created by hungnguyen on 8/16/17.
 */

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hoocons.hoocons_android.ViewFragments.ChannelAboutFragment;
import com.hoocons.hoocons_android.ViewFragments.ChannelContentFragment;
import com.hoocons.hoocons_android.ViewFragments.ChannelDetailFragment;
import com.hoocons.hoocons_android.ViewFragments.ChannelMeetoutFragment;
import com.hoocons.hoocons_android.ViewFragments.ChannelThreadsFragment;

import java.util.ArrayList;
import java.util.List;

public class ChannelViewPagerAdapter  extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitle = new ArrayList<>();
    private final int MAX_FRAGMENTS = 5;

    private ChannelAboutFragment channelAboutFragment;
    private ChannelThreadsFragment channelThreadsFragment;
    private ChannelContentFragment channelContentFragment;
    private ChannelMeetoutFragment channelMeetoutFragment;
    private ChannelDetailFragment channelDetailFragment;

    public Context mContext;

    public ChannelViewPagerAdapter(Context mContext, FragmentManager manager) {
        super(manager);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (channelAboutFragment == null)
                    channelAboutFragment = ChannelAboutFragment.newInstance("0", "test");
                return channelAboutFragment;
            case 1:
                if (channelThreadsFragment == null)
                    channelThreadsFragment = ChannelThreadsFragment.newInstance("1", "test");
                return channelThreadsFragment;
            case 2:
                if (channelContentFragment == null) {
                    channelContentFragment = ChannelContentFragment.newInstance("2", "test");
                }
                return channelContentFragment;
            case 3:
                if (channelMeetoutFragment == null) {
                    channelMeetoutFragment = ChannelMeetoutFragment.newInstance("3", "test");
                }
                return channelMeetoutFragment;
            case 4:
                if (channelDetailFragment == null) {
                    channelDetailFragment = ChannelDetailFragment.newInstance("4", "test");
                }
                return channelDetailFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return MAX_FRAGMENTS;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return null;
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitle.add(title);
    }
}
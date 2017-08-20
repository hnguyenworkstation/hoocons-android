package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hoocons.hoocons_android.ViewFragments.CommunicationFragment;
import com.hoocons.hoocons_android.ViewFragments.FeaturedFragment;
import com.hoocons.hoocons_android.ViewFragments.MoreFragment;
import com.hoocons.hoocons_android.ViewFragments.NotificationFragment;
import com.hoocons.hoocons_android.ViewFragments.DiscoverFragment;
import com.hoocons.hoocons_android.ViewFragments.PlayGroundFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungnguyen on 6/3/17.
 */
public class MainViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitle = new ArrayList<>();
    private final int MAX_FRAGMENTS = 5;

    private FeaturedFragment featuredFragment;
    private DiscoverFragment discoverFragment;
    private CommunicationFragment communicationFragment;
    private PlayGroundFragment playGroundFragment;
    private NotificationFragment notificationFragment;
    private MoreFragment moreFragment;

    private Context mContext;

    public MainViewPagerAdapter(Context mContext, FragmentManager manager) {
        super(manager);
        this.mContext = mContext;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                if (featuredFragment == null)
                    featuredFragment = FeaturedFragment.newInstance("0", "test");
                return featuredFragment;
            case 1:
                if (discoverFragment == null)
                    discoverFragment = DiscoverFragment.newInstance("0", "test");
                return discoverFragment;
            case 2:
                if (playGroundFragment == null) {
                    playGroundFragment = PlayGroundFragment.newInstance("0", "test");
                }
                return playGroundFragment;
            case 3:
                if (communicationFragment == null) {
                    communicationFragment = CommunicationFragment.newInstance("0", "test");
                }
                return communicationFragment;
            case 4:
                if (moreFragment == null) {
                    moreFragment = new MoreFragment();
                }
                return moreFragment;
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

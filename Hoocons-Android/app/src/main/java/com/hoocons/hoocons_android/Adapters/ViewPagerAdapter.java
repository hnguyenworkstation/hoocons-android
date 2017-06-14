package com.hoocons.hoocons_android.Adapters;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.hoocons.hoocons_android.ViewFragments.AroundFragment;
import com.hoocons.hoocons_android.ViewFragments.CommunicationFragment;
import com.hoocons.hoocons_android.ViewFragments.FeaturedFragment;
import com.hoocons.hoocons_android.ViewFragments.MoreFragment;
import com.hoocons.hoocons_android.ViewFragments.NotificationFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungnguyen on 6/3/17.
 */
public class ViewPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();
    private List<String> mFragmentTitle = new ArrayList<>();
    private final int MAX_FRAGMENTS = 5;

    private FeaturedFragment featuredFragment;
    private AroundFragment aroundFragment;
    private CommunicationFragment communicationFragment;
    private NotificationFragment notificationFragment;
    private MoreFragment moreFragment;

    public Context mContext;

    public ViewPagerAdapter(Context mContext, FragmentManager manager) {
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
                if (aroundFragment == null)
                    aroundFragment = AroundFragment.newInstance("0", "test");
                return aroundFragment;
            case 2:
                if (communicationFragment == null) {
                    communicationFragment = CommunicationFragment.newInstance("0", "test");
                }
                return communicationFragment;
            case 3:
                if (notificationFragment == null) {
                    notificationFragment = NotificationFragment.newInstance("0", "test");
                }
                return notificationFragment;
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

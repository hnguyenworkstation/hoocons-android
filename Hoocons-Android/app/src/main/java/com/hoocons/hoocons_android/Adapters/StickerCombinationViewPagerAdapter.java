package com.hoocons.hoocons_android.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.hoocons.hoocons_android.ViewFragments.EmotionFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hungnguyen on 7/26/17.
 */

public class StickerCombinationViewPagerAdapter extends FragmentStatePagerAdapter {
    private List<Fragment> mFragmentList = new ArrayList<>();

    public StickerCombinationViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }

    public String getPageTitle(int i) {
        return "item " + (i + 1);
    }

    public void addFragment(Fragment fragment) {
        mFragmentList.add(fragment);
    }
}

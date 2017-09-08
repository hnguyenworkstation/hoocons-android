package com.hoocons.hoocons_android.Activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.hoocons.hoocons_android.Adapters.MainViewPagerAdapter;
import com.hoocons.hoocons_android.CustomUI.CustomViewPager;
import com.hoocons.hoocons_android.Helpers.PermissionUtils;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.ViewFragments.CommunicationFragment;
import com.hoocons.hoocons_android.ViewFragments.DiscoverFragment;
import com.hoocons.hoocons_android.ViewFragments.FeaturedFragment;
import com.hoocons.hoocons_android.ViewFragments.PlayGroundFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    @BindView(R.id.viewpager)
    CustomViewPager mViewPager;
    @BindView(R.id.tab_globe)
    RelativeLayout mTabGlobe;
    @BindView(R.id.tab_hot)
    RelativeLayout mTabHot;
    @BindView(R.id.tab_play)
    RelativeLayout mTabPlay;
    @BindView(R.id.tab_chat)
    RelativeLayout mTabChat;
    @BindView(R.id.tab_menu)
    RelativeLayout mTabMenu;

    @BindView(R.id.bottom_tab_globe)
    ImageView mImageTabGlobe;
    @BindView(R.id.bottom_tab_hot)
    ImageView mImageTabHot;
    @BindView(R.id.bottom_tab_play)
    ImageView mImageTabPlay;
    @BindView(R.id.bottom_tab_chat)
    ImageView mImageTabChat;
    @BindView(R.id.bottom_tab_menu)
    ImageView mImageTabMenu;

    private MainViewPagerAdapter mMainViewPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.fix_anim, R.anim.slide_out_to_bottom);

        ButterKnife.bind(this);

        initViewPager();
        initClickListener();
        initViewAt(0);
    }

    private void initClickListener() {
        mTabGlobe.setOnClickListener(this);
        mTabHot.setOnClickListener(this);
        mTabChat.setOnClickListener(this);
        mTabPlay.setOnClickListener(this);
        mTabMenu.setOnClickListener(this);
    }

    /*
    * SETTING UP BOTTOM BAR
    * */
    private void initViewPager() {
        mMainViewPagerAdapter = new MainViewPagerAdapter(this.getBaseContext(), getSupportFragmentManager());

        /** the ViewPager requires a minimum of 1 as OffscreenPageLimit */
        int limit = (mMainViewPagerAdapter.getCount() > 1 ? mMainViewPagerAdapter.getCount() - 1 : 1);

        mViewPager.setAdapter(mMainViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                invalidateFragmentMenus(position);
            }

            @Override
            public void onPageSelected(int position){
                invalidateFragmentMenus(position);

                switch (position) {
                    case 0:
                        break;
                    case 1:
                        DiscoverFragment dTemp = (DiscoverFragment) mMainViewPagerAdapter.getItem(position);
                        if (dTemp != null)
                            dTemp.onRestore();
                        break;
                    case 2:
                        PlayGroundFragment playGroundFragment = (PlayGroundFragment) mMainViewPagerAdapter.getItem(position);
                        if (playGroundFragment != null) {
                            playGroundFragment.onRestore();
                        }
                        break;
                    case 3:
                        CommunicationFragment cTemp = (CommunicationFragment) mMainViewPagerAdapter.getItem(position);
                        if (cTemp != null) {
                            cTemp.onRestore();
                        }
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        mViewPager.setOffscreenPageLimit(limit);
    }

    /*
    * CHANGING MAIN MENU EACH TIME WE CHANGE A FRAGMENT
    * MENUS ARE SET FROM EACH MENU
    * */
    private void invalidateFragmentMenus(int position){
        for(int i = 0; i < mMainViewPagerAdapter.getCount(); i++){
            mMainViewPagerAdapter.getItem(i).setHasOptionsMenu(i == position);
        }
        invalidateOptionsMenu(); //or respectively its support method.
    }

    private void initViewAt(int position) {
        switch (position) {
            case 0:
                mImageTabGlobe.setSelected(true);
                mImageTabHot.setSelected(false);
                mImageTabPlay.setSelected(false);
                mImageTabChat.setSelected(false);
                mImageTabMenu.setSelected(false);
                mViewPager.setCurrentItem(0);
                break;
            case 1:
                mImageTabGlobe.setSelected(false);
                mImageTabHot.setSelected(true);
                mImageTabPlay.setSelected(false);
                mImageTabChat.setSelected(false);
                mImageTabMenu.setSelected(false);
                mViewPager.setCurrentItem(1);
                break;
            case 2:
                mImageTabGlobe.setSelected(false);
                mImageTabHot.setSelected(false);
                mImageTabPlay.setSelected(true);
                mImageTabChat.setSelected(false);
                mImageTabMenu.setSelected(false);
                mViewPager.setCurrentItem(2);
                break;
            case 3:
                mImageTabGlobe.setSelected(false);
                mImageTabHot.setSelected(false);
                mImageTabPlay.setSelected(false);
                mImageTabChat.setSelected(true);
                mImageTabMenu.setSelected(false);
                mViewPager.setCurrentItem(3);
                break;
            case 4:
                mImageTabGlobe.setSelected(false);
                mImageTabHot.setSelected(false);
                mImageTabPlay.setSelected(false);
                mImageTabChat.setSelected(false);
                mImageTabMenu.setSelected(true);
                mViewPager.setCurrentItem(4);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab_globe:
                initViewAt(0);
                break;
            case R.id.tab_hot:
                initViewAt(1);
                break;
            case R.id.tab_play:
                initViewAt(2);
                break;
            case R.id.tab_chat:
                initViewAt(3);
                break;
            case R.id.tab_menu:
                initViewAt(4);
                break;
            default:
                break;
        }
    }
}

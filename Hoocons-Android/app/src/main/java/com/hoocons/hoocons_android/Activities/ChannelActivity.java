package com.hoocons.hoocons_android.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.github.ppamorim.dragger.DraggerActivity;
import com.hoocons.hoocons_android.Adapters.ChannelViewPagerAdapter;
import com.hoocons.hoocons_android.Adapters.MainViewPagerAdapter;
import com.hoocons.hoocons_android.CustomUI.BottomNavigationViewHelper;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChannelActivity extends DraggerActivity {
    @BindView(R.id.channel_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomTabbar;

    private ChannelViewPagerAdapter mViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        ButterKnife.bind(this);

        initBottomBar();

        setDraggerLimit(0.8f);
        setSlideEnabled(true);
    }

    private void initBottomBar() {
        mViewPagerAdapter = new ChannelViewPagerAdapter(this.getBaseContext(),
                getSupportFragmentManager());

        /** the ViewPager requires a minimum of 1 as OffscreenPageLimit */
        int limit = (mViewPagerAdapter.getCount() > 1 ? mViewPagerAdapter.getCount() - 1 : 1);

        mViewPager.setAdapter(mViewPagerAdapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                invalidateFragmentMenus(position);
            }

            @Override
            public void onPageSelected(int position){
                invalidateFragmentMenus(position);
                mBottomTabbar.getMenu().getItem(position).setChecked(true);

                switch (position) {
                    case 0:
                        // getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                        if (mViewPagerAdapter.getItem(position) == null) {

                        }
                        break;
                    default:
                        // getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setOffscreenPageLimit(limit);

        /*
        * Setting up the bottom tabbar with viewpager
        * */
        if (mBottomTabbar != null) {
            mBottomTabbar.setOnNavigationItemSelectedListener(new BottomNavigationView
                    .OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()){
                        case R.id.featured:
                            mViewPager.setCurrentItem(0);
                            break;
                        case R.id.nearme:
                            mViewPager.setCurrentItem(1);
                            break;
                        case R.id.chat_list:
                            mViewPager.setCurrentItem(2);
                            break;
                        case R.id.notification:
                            mViewPager.setCurrentItem(3);
                            break;
                        case R.id.action_more:
                            mViewPager.setCurrentItem(4);
                            break;
                        default:
                            break;
                    }
                    return false;
                }
            });
        }
    }

    /*
    * CHANGING MAIN MENU EACH TIME WE CHANGE A FRAGMENT
    * MENUS ARE SET FROM EACH MENU
    * */
    private void invalidateFragmentMenus(int position){
        for(int i = 0; i < mViewPagerAdapter.getCount(); i++){
            mViewPagerAdapter.getItem(i).setHasOptionsMenu(i == position);
        }
        invalidateOptionsMenu(); //or respectively its support method.
    }
}

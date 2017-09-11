package com.hoocons.hoocons_android.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import com.github.ppamorim.dragger.DraggerActivity;
import com.hoocons.hoocons_android.Adapters.ChannelViewPagerAdapter;
import com.hoocons.hoocons_android.EventBus.AllowSlideDown;
import com.hoocons.hoocons_android.EventBus.BlockSlideDown;
import com.hoocons.hoocons_android.EventBus.ChannelProfileDataBus;
import com.hoocons.hoocons_android.Parcel.ChannelProfileParcel;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.parceler.Parcels;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ChannelActivity extends DraggerActivity {
    @BindView(R.id.channel_viewpager)
    ViewPager mViewPager;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView mBottomTabbar;

    private ChannelViewPagerAdapter mViewPagerAdapter;
    private ChannelProfileParcel channelProfileParcel;
    private boolean isFirstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_channel);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        if (intent != null) {
            channelProfileParcel = (ChannelProfileParcel)
                    Parcels.unwrap(intent.getParcelableExtra("channel_profile"));
        }

        if (channelProfileParcel != null && isFirstLoad) {
            isFirstLoad = false;
            EventBus.getDefault().post(new ChannelProfileDataBus(channelProfileParcel));
        }

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
                        case R.id.channel_feeds:
                            mViewPager.setCurrentItem(0);
                            break;
                        case R.id.channel_threads:
                            mViewPager.setCurrentItem(1);
                            break;
                        case R.id.channel_meetouts:
                            mViewPager.setCurrentItem(2);
                            break;
                        case R.id.channel_content:
                            mViewPager.setCurrentItem(3);
                            break;
                        case R.id.channel_more:
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

    @Override
    protected void onResume() {
        super.onResume();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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


    /*
    * EVENT BUSES CATCHING
    * */
    @Subscribe
    public void onEvent(AllowSlideDown slideDown) {
        setSlideEnabled(true);
    }

    @Subscribe
    public void onEvent(BlockSlideDown blockSlideDown) {
        setSlideEnabled(false);
    }
}

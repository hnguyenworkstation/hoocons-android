package com.hoocons.hoocons_android.Activities;

import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.hoocons.hoocons_android.Adapters.ViewPagerAdapter;
import com.hoocons.hoocons_android.CustomUI.BottomNavigationViewHelper;
import com.hoocons.hoocons_android.Managers.BaseActivity;
import com.hoocons.hoocons_android.R;
import com.hoocons.hoocons_android.SlidingMenu.SlidingMenu;
import com.hoocons.hoocons_android.SlidingMenu.app.SlidingActivity;
import com.hoocons.hoocons_android.SlidingMenu.app.SlidingFragmentActivity;
import com.hoocons.hoocons_android.ViewFragments.AroundFragment;
import com.hoocons.hoocons_android.ViewFragments.CommunicationFragment;
import com.hoocons.hoocons_android.ViewFragments.FeaturedFragment;
import com.hoocons.hoocons_android.ViewFragments.MoreFragment;
import com.hoocons.hoocons_android.ViewFragments.NotificationFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {
    @BindView(R.id.viewpager)
    ViewPager mViewPager;
    @BindView(R.id.tablayout)
    TabLayout mTabLayout;

    private ViewPagerAdapter mViewPagerAdapter;

    private final int[] mTabsIcons = {
            R.drawable.ic_tab_home,
            R.drawable.ic_tab_near_me,
            R.drawable.ic_tab_chat,
            R.drawable.ic_tab_notification,
            R.drawable.ic_tab_setting
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.fix_anim, R.anim.slide_out_to_bottom);

        ButterKnife.bind(this);

        initViewPager();
        initTabBar();
    }


    /*
    * SETTING UP BOTTOM BAR
    * */
    private void initViewPager() {
        mViewPagerAdapter = new ViewPagerAdapter(this.getBaseContext(), getSupportFragmentManager());

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

                switch (position) {
                    case 0:
                        // getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
                        if (mViewPagerAdapter.getItem(position) == null) {
                            mViewPagerAdapter.addFragment(new FeaturedFragment(), "Featured");
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
    }


    private void initTabBar() {
        mTabLayout.setupWithViewPager(mViewPager);
        if (mTabLayout != null) {
            mTabLayout.setupWithViewPager(mViewPager);
            for (int i = 0; i < mTabLayout.getTabCount(); i++) {
                mTabLayout.getTabAt(i).setIcon(mTabsIcons[i]);
            }
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

package com.hoocons.hoocons_android.ViewFragments;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.hoocons.hoocons_android.Adapters.StickerCombinationViewPagerAdapter;
import com.hoocons.hoocons_android.CustomUI.SlidingTabLayout;
import com.hoocons.hoocons_android.EventBus.SmallEmotionClicked;
import com.hoocons.hoocons_android.Interface.OnStickerChildInteractionListener;
import com.hoocons.hoocons_android.Models.Emotion;
import com.hoocons.hoocons_android.R;

import org.greenrobot.eventbus.EventBus;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StickerCombinationFragment extends Fragment implements OnStickerChildInteractionListener {
    @BindView(R.id.vpPager)
    ViewPager mViewPager;
    @BindView(R.id.sliding_tabs)
    SlidingTabLayout mSlidingTabLayout;

    private Animation mAnimationOut;
    private Animation mAnimationIn;
    private View horizontalBar;
    private boolean isAnimating = false;

    private Timer timer;
    protected int increate = 0;
    private static final long TIMER_PERIOD = 500;
    private static final long TIMER_DELAY = 0;

    private EmotionFragment emotionFragment;
    private PopoStickerFragment popoStickerFragment;
    private MonkeyStickerFragment monkeyStickerFragment;

    public static StickerCombinationFragment newInstance() {
        StickerCombinationFragment fragment = new StickerCombinationFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public StickerCombinationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sticker_combination, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        StickerCombinationViewPagerAdapter pageAdapter =
                new StickerCombinationViewPagerAdapter(getChildFragmentManager());

        // Init Emotion fragment
        emotionFragment = EmotionFragment.newInstance();
        emotionFragment.setOnEmotionListener(new EmotionFragment.OnEmotionSelectedListener() {
            @Override
            public void onEmotionSelected(Emotion emotion) {
                EventBus.getDefault().post(new SmallEmotionClicked(emotion));
            }
        });
        pageAdapter.addFragment(emotionFragment);

        // Init Popo Sticker Fragment
        popoStickerFragment = PopoStickerFragment.newInstance();
        popoStickerFragment.setOnEmotionListener(new PopoStickerFragment.OnPoPoEmotionSelectedListener() {
            @Override
            public void onPoPoEmotionSelected(Emotion emotion) {

            }
        });
        pageAdapter.addFragment(popoStickerFragment);
        
//        // Init Monkey sticker fragment
//        monkeyStickerFragment = MonkeyStickerFragment.newInstance();
//        pageAdapter.addFragment(monkeyStickerFragment);

        mViewPager.setAdapter(pageAdapter);



        // BEGIN_INCLUDE (setup_slidingtablayout)
        // Give the SlidingTabLayout the ViewPager, this must be done AFTER the ViewPager has had
        // it's PagerAdapter set.
        mSlidingTabLayout = (SlidingTabLayout) view.findViewById(R.id.sliding_tabs);
        mSlidingTabLayout.setCustomTabView(R.layout.horizontal_item, R.id.content);
        mSlidingTabLayout.setViewPager(mViewPager);
        mSlidingTabLayout.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                onScrollDown(false);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        // END_INCLUDE (setup_slidingtablayout)


        setupHorizontalBarAnimation(view);
    }

    private void setupHorizontalBarAnimation(View v) {
        horizontalBar = v.findViewById(R.id.scroll);
        mAnimationIn = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_in_bottom_up);
        mAnimationOut = AnimationUtils.loadAnimation(getActivity(),
                R.anim.slide_out_top_down);
        mAnimationIn.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                horizontalBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isAnimating = false;
                deleteTimer();
            }
        });

        mAnimationOut.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                horizontalBar.setVisibility(View.INVISIBLE);
                isAnimating = false;
                // Start timer
                setupTimer();
            }
        });

    }

    private void deleteTimer() {
        if (timer != null) {
            timer.cancel();
            timer.purge();
            timer = null;
        }
    }

    /**
     * Setup timer for bottom bar animation
     */
    private void setupTimer() {
        if (horizontalBar.getVisibility() != View.VISIBLE) {
            deleteTimer();
            timer = new Timer();
            // Create timer task for bottom bar
            TimerTask myTimerTask = new TimerTask() {

                @Override
                public void run() {
                    if (increate >= 3) {
                        onScrollDown(false);
                        increate = 0;
                    } else {
                        increate++;
                    }
                }
            };
            timer.schedule(myTimerTask, TIMER_DELAY, TIMER_PERIOD);
        }
    }

    @Override
    public void onScrollDown(boolean isScrollDown) {
        if (isAnimating)
            return;
        if (isScrollDown) {
            if (horizontalBar.getVisibility() == View.VISIBLE) {
                isAnimating = true;
                Handler refresh = new Handler(Looper.getMainLooper());
                refresh.postDelayed(new Runnable() {
                    public void run() {
                        horizontalBar.startAnimation(mAnimationOut);
                    }
                }, 75);
            }
        } else {
            if (horizontalBar.getVisibility() != View.VISIBLE) {
                isAnimating = true;
                Handler refresh = new Handler(Looper.getMainLooper());
                refresh.postDelayed(new Runnable() {
                    public void run() {
                        horizontalBar.startAnimation(mAnimationIn);
                    }
                }, 75);
            }
        }
    }

    @Override
    public void resetCountUpAnimation() {
        increate = 0;
    }

    @Override
    public void clickOnItem(int i) {

    }
}

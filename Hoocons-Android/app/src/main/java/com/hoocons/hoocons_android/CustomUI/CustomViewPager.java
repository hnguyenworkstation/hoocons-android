package com.hoocons.hoocons_android.CustomUI;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

/**
 * Created by hungnguyen on 8/20/17.
 */
public class CustomViewPager  extends ViewPager {
    private boolean isVertialViewpagerIndex;

    public CustomViewPager(Context context) {
        super(context);
        setCustomScroller();
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setCustomScroller();
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        if (isVertialViewpagerIndex == true) {
            return super.onInterceptTouchEvent(event);
        }

        return false;
    }

    private void setIsVertialViewPager(boolean isVertialViewPager) {
        this.isVertialViewpagerIndex = isVertialViewPager;
    }

    private void setCustomScroller() {
        try {
            Class<?> viewpager = ViewPager.class;
            Field scroller = viewpager.getDeclaredField("mScroller");
            scroller.setAccessible(true);
            scroller.set(this, new MyScroller(getContext()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class MyScroller extends Scroller {
        public MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/);
        }
    }
}
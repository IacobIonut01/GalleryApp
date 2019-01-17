package com.dot.gallery.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.animation.DecelerateInterpolator;
import android.widget.Scroller;

import java.lang.reflect.Field;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {

    private boolean viewScroll = false;

    public CustomViewPager(Context context) {
        super(context);
        setMyScroller();
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMyScroller();
    }

    public boolean isScroll() {
        return viewScroll;
    }

    public void setScroll(boolean viewScroll) {
        this.viewScroll = viewScroll;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {
            return viewScroll && super.onInterceptTouchEvent(event);
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            return viewScroll && super.onTouchEvent(event);
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private void setMyScroller() {
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
        MyScroller(Context context) {
            super(context, new DecelerateInterpolator());
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, 350 /*1 secs*/);
        }
    }
}

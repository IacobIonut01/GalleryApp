package com.dot.gallery.views;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;

import com.bogdwellers.pinchtozoom.ImageMatrixCorrector;
import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;

public class PagerMatrixTouchHandler extends ImageMatrixTouchHandler {

    CustomViewPager viewPager;

    public PagerMatrixTouchHandler(Context context) {
        super(context);
    }

    public PagerMatrixTouchHandler(Context context, ImageMatrixCorrector corrector) {
        super(context, corrector);
    }

    public void setViewPager(CustomViewPager viewPager) {
        this.viewPager = viewPager;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        if (getMode() == PINCH || getMode() == MORPH)
            viewPager.setScroll(false);
        else
            viewPager.setScroll(true);
        return super.onTouch(view, event);
    }
}

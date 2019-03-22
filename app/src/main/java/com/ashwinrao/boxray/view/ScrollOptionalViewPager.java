package com.ashwinrao.boxray.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

public class ScrollOptionalViewPager extends ViewPager {

    private boolean scrollingEnabled;

    public ScrollOptionalViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.scrollingEnabled = false;
    }

    public void setScrollingBehavior(boolean scrollingEnabled) {
        this.scrollingEnabled = scrollingEnabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(scrollingEnabled) { return super.onInterceptTouchEvent(ev); }
        else { return false; }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(scrollingEnabled) { return super.onTouchEvent(ev); }
        else { return false; }
    }

}

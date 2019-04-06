package com.ashwinrao.boxray.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.viewpager.widget.ViewPager;

public class ScrollOptionalViewPager extends ViewPager {

    private boolean scrollingEnabled;
    private MutableLiveData<Boolean> wasSwiped;

    public ScrollOptionalViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.scrollingEnabled = false;
        wasSwiped = new MutableLiveData<>();
        wasSwiped.setValue(false);
    }

    public LiveData<Boolean> getWasSwiped() {
        return wasSwiped;
    }

    public void setScrollingBehavior(boolean scrollingEnabled) {
        this.scrollingEnabled = scrollingEnabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        wasSwiped.setValue(true);
        if(scrollingEnabled) { return super.onInterceptTouchEvent(ev); }
        else { return false; }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        wasSwiped.setValue(true);
        if(scrollingEnabled) { return super.onTouchEvent(ev); }
        else { return false; }
    }

    private boolean checkIfDidRegisterSwipe(MotionEvent event) {
        float x1 = 0;
        final int MIN_DISTANCE = 150;

        switch(event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x1 = event.getX();
                return true;
            case MotionEvent.ACTION_UP:
                float x2 = event.getX();
                float deltaX = x2 - x1;
                if (Math.abs(deltaX) > MIN_DISTANCE) { wasSwiped.setValue(true); }
                return true;
        }
        return false;
    }

}

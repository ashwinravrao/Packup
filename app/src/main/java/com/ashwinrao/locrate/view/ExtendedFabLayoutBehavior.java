package com.ashwinrao.locrate.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import static com.ashwinrao.locrate.util.UnitConversion.dpToPx;

public class ExtendedFabLayoutBehavior extends CoordinatorLayout.Behavior<ExtendedFloatingActionButton> {

    public ExtendedFabLayoutBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean layoutDependsOn(@NonNull CoordinatorLayout parent, @NonNull ExtendedFloatingActionButton child, @NonNull View dependency) {
        return dependency instanceof AppBarLayout;
    }

    @Override
    public boolean onDependentViewChanged(@NonNull CoordinatorLayout parent, @NonNull ExtendedFloatingActionButton child, @NonNull View dependency) {
        if(dependency instanceof AppBarLayout) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) child.getLayoutParams();
            int fabBottomMargin = lp.bottomMargin;
            int distanceToScroll = child.getHeight() + fabBottomMargin;
            float ratio = dependency.getY()/(float) dpToPx(parent.getContext(), 32);
            child.setTranslationY(-distanceToScroll * ratio);
        }
        return true;
    }
}

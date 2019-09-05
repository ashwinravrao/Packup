package com.ashwinrao.locrate.util;

import android.util.Log;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;


public class RecyclerViewDimensionHelper {

    private static ConstraintLayout.LayoutParams params;
    private static int originalHeight;
    private static final String TAG = "RV";

    public static void freezeDimensions(@NonNull final RecyclerView recyclerView) {

        // get view dimensions
        final ViewTreeObserver viewTreeObserver = recyclerView.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    recyclerView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    originalHeight = recyclerView.getHeight();
                    Log.d(TAG, "onGlobalLayout: Height: " + originalHeight);
                }
            });
        }

        // set view dimensions
        params = (ConstraintLayout.LayoutParams) recyclerView.getLayoutParams();
        params.height = originalHeight;
        recyclerView.setLayoutParams(params);

//        new Handler().postDelayed(() -> thawDimensions(recyclerView), 2000);

    }

    private static void thawDimensions(@NonNull final RecyclerView recyclerView) {

        // restore view dimensions
        params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        recyclerView.setLayoutParams(params);
    }
}

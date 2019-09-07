package com.ashwinrao.packup.util;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static com.ashwinrao.packup.util.UnitConversion.dpToPx;

public class Decorations {

    public static void addItemDecoration(Context context, RecyclerView recyclerView, final int spanCount) {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                int position = parent.getChildAdapterPosition(view);
                int spacing = dpToPx(context, 16f);

                if (position >= 0) {
                    int column = position % spanCount;

                    outRect.left = spacing - column * spacing / spanCount;
                    outRect.right = (column + 1) * spacing / spanCount;

                    if (position < spanCount) {
                        outRect.top = spacing;
                    }
                    outRect.bottom = spacing;
                } else {
                    outRect.left = 0;
                    outRect.right = 0;
                    outRect.top = 0;
                    outRect.bottom = 0;
                }
            }
        });
    }

    public static void simpleItemDecoration(Context context, RecyclerView recyclerView, boolean skipApplyLastMargin) {
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

                int position = parent.getChildAdapterPosition(view);

                if(position == 2 && skipApplyLastMargin) {  // all child views except last one
                    outRect.right = 0;
                } else {
                    outRect.right = dpToPx(context, 16f);
                }
            }
        });
    }
}

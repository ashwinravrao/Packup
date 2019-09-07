package com.ashwinrao.packup.util;

import android.content.Context;
import android.util.TypedValue;

public class UnitConversion {

    public static int dpToPx(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}

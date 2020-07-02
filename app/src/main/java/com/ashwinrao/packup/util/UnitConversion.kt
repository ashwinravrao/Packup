package com.ashwinrao.packup.util

import android.content.Context
import android.util.TypedValue

object UnitConversion {

    @JvmStatic
    fun dpToPx(context: Context, dp: Float): Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()

}

package com.ashwinrao.packup.util

import android.content.Context
import android.util.TypedValue

class UnitConversion {

    companion object {

        @JvmStatic
        fun dpToPx(context: Context, dp: Float) : Int = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.resources.displayMetrics).toInt()

    }
}

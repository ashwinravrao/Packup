package com.ashwinrao.packup.util

import android.content.Context
import android.view.View
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import com.ashwinrao.packup.R

object HideShowNotch {

    /**
     * Choose the correct status bar and icon color based on some obtained criterion value.
     * This is useful, for example, when observing the CheckedChangedListener of a CheckBox.
     */

    @JvmStatic
    fun contextSpecific(context: Context, window: Window, criterion: Boolean, lightBackground: Int = R.color.colorPrimary, darkIconsOnLight: Boolean = true) {
        if (criterion) dark(context, window)
        else light(context, window, lightBackground, darkIconsOnLight)
    }

    /**
     * Manually set dark status bar with light icons.
     */

    @JvmStatic
    fun dark(context: Context, window: Window) {
        window.decorView.systemUiVisibility = window.decorView.systemUiVisibility.and(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
        window.statusBarColor = context.resources.getColor(android.R.color.black, context.theme)
    }

    /**
     * Manually set light status bar with dark icons. Optional background and icon color params.
     */

    @JvmStatic
    fun light(context: Context, window: Window, lightBackground: Int = R.color.colorPrimary, darkIconsOnLight: Boolean = true) {
        window.decorView.systemUiVisibility =
                if (darkIconsOnLight) window.decorView.systemUiVisibility.or(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
                else window.decorView.systemUiVisibility.and(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv())
        window.statusBarColor = context.resources.getColor(lightBackground, context.theme)
    }

    /**
     * Retrieves notch hide/show preference from the global shared preferences file
     * and applies to the status bar for the current Activity.
     */

    @JvmStatic
    fun apply(context: Context, window: Window, lightBackground: Int = R.color.colorPrimary, darkIconsOnLight: Boolean = true): Boolean {
        val shouldHideNotch = SettingsUtil.getHideNotchSetting(context)
        contextSpecific(context, window, shouldHideNotch, lightBackground, darkIconsOnLight)
        return shouldHideNotch
    }

    @JvmStatic
    fun applyThemeIfAvailable(activity: AppCompatActivity): Boolean {
        return if (SettingsUtil.getHideNotchSetting(activity.applicationContext)) {
            activity.setTheme(R.style.AppTheme_NoActionBar_HideNotch)
            true
        } else false
    }
}
package com.ashwinrao.packup.util

import android.transition.Fade
import android.view.Window

object WindowUtil {

    /**
     * Excludes system bars from participating in an activity's SharedElement fade transition.
     */

    @JvmStatic
    fun transitionExcludeBars(window: Window) {
        val fade = Fade()
        fade.excludeTarget(android.R.id.statusBarBackground, true)
        fade.excludeTarget(android.R.id.navigationBarBackground, true)
        window.enterTransition = fade
    }

}
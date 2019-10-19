package com.ashwinrao.packup.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.ashwinrao.packup.R

object SettingsUtil {

    @JvmStatic
    fun getShutterVibrationSetting(context: Context): Boolean {
        return context.applicationContext.getSharedPreferences(context.getString(R.string.settings_shared_preference), MODE_PRIVATE)
                .getBoolean(context.getString(R.string.shutter_vibration_key), true)
    }

    @JvmStatic
    fun writeShutterVibrationSetting(context: Context, isChecked: Boolean) {
        context.applicationContext
                .getSharedPreferences(context.getString(R.string.settings_shared_preference), MODE_PRIVATE)
                .edit()
                .putBoolean(context.getString(R.string.shutter_vibration_key), isChecked)
                .apply()
    }

    @JvmStatic
    fun getHideNotchSetting(context: Context): Boolean {
        return context.applicationContext.getSharedPreferences(context.getString(R.string.settings_shared_preference), MODE_PRIVATE)
                .getBoolean(context.getString(R.string.hide_notch_key), false)
    }

    @JvmStatic
    fun writeHideNotchSetting(context: Context, isChecked: Boolean) {
        context.applicationContext
                .getSharedPreferences(context.getString(R.string.settings_shared_preference), MODE_PRIVATE)
                .edit()
                .putBoolean(context.getString(R.string.hide_notch_key), isChecked)
                .apply()
    }

}
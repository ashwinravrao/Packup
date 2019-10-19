
package com.ashwinrao.packup

import android.app.Application
import android.content.Context
import com.ashwinrao.packup.di.AppComponent
import com.ashwinrao.packup.di.DaggerAppComponent
import com.ashwinrao.packup.di.DatabaseModule

class Packup : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent =
                DaggerAppComponent
                        .builder()
                        .databaseModule(DatabaseModule(this))
                        .build()
    }

    fun retrieveNotchPreference(): Boolean {
        return this
                .getSharedPreferences(getString(R.string.settings), Context.MODE_PRIVATE)
                .getBoolean(getString(R.string.hide_notch_key), false)
    }

}
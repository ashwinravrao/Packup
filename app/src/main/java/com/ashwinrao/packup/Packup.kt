
package com.ashwinrao.packup

import android.app.Application
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
}
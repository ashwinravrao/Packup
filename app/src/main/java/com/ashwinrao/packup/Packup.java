package com.ashwinrao.packup;

import android.app.Application;

import com.ashwinrao.packup.di.AppComponent;
import com.ashwinrao.packup.di.DaggerAppComponent;
import com.ashwinrao.packup.di.DatabaseModule;

public class Packup extends Application {

    AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        appComponent = DaggerAppComponent.builder().databaseModule(new DatabaseModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }
}

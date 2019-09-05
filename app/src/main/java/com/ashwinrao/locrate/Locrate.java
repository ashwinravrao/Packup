package com.ashwinrao.locrate;

import android.app.Application;

import com.ashwinrao.locrate.di.AppComponent;
import com.ashwinrao.locrate.di.DaggerAppComponent;
import com.ashwinrao.locrate.di.DatabaseModule;

public class Locrate extends Application {

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

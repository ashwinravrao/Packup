package com.ashwinrao.boxray;

import android.app.Application;

import com.ashwinrao.boxray.data.BoxDatabase;
import com.ashwinrao.boxray.data.BoxRepository;
import com.ashwinrao.boxray.di.AppComponent;
import com.ashwinrao.boxray.di.DaggerAppComponent;
import com.ashwinrao.boxray.di.DatabaseModule;

public class Boxray extends Application {

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

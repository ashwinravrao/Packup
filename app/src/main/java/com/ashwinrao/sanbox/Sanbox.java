package com.ashwinrao.sanbox;

import android.app.Application;

import com.ashwinrao.sanbox.di.AppComponent;
import com.ashwinrao.sanbox.di.DaggerAppComponent;
import com.ashwinrao.sanbox.di.DatabaseModule;

public class Sanbox extends Application {

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

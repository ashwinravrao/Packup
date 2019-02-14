package com.ashwinrao.boxray;

import android.app.Application;

import com.ashwinrao.boxray.data.BoxDatabase;
import com.ashwinrao.boxray.data.BoxRepository;

public class Boxray extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public BoxDatabase getDatabase() {
        return BoxDatabase.getInstance(this);
    }

    public BoxRepository getRepository() {
        return BoxRepository.getInstance(getDatabase());
    }
}

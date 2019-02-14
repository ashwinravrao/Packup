package com.ashwinrao.boxray.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BoxViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static BoxViewModelFactory sInstance;
    private Application mApplication;

    public static synchronized BoxViewModelFactory getInstance(@NonNull Application application) {
        if(sInstance == null) {
            sInstance = new BoxViewModelFactory(application);
        }
        return sInstance;
    }

    private BoxViewModelFactory(@NonNull Application application) {
        super();
        mApplication = application;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BoxViewModel(mApplication);
    }
}

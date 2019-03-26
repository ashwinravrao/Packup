package com.ashwinrao.boxray.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class BoxViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static BoxViewModelFactory instance;
    private Application application;

    public static synchronized BoxViewModelFactory getInstance(@NonNull Application application) {
        if(instance == null) {
            instance = new BoxViewModelFactory(application);
        }
        return instance;
    }

    private BoxViewModelFactory(@NonNull Application application) {
        super();
        this.application = application;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BoxViewModel(application);
    }
}

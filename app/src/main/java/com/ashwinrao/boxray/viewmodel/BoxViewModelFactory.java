package com.ashwinrao.boxray.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.data.BoxRepository;

public class BoxViewModelFactory extends ViewModelProvider.NewInstanceFactory {

    private static BoxViewModelFactory instance;
    private final BoxRepository repo;

    public static synchronized BoxViewModelFactory getInstance(@NonNull Application application) {
        if(instance == null) {
            instance = new BoxViewModelFactory(application);
        }
        return instance;
    }

    private BoxViewModelFactory(@NonNull Application application) {
        this.repo = ((Boxray) application).getRepository();
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BoxViewModel(repo);
    }
}

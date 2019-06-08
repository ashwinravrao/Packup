package com.ashwinrao.boxray.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ashwinrao.boxray.data.BoxRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final BoxRepository repo;

    @Inject
    public ViewModelFactory(@NonNull BoxRepository repo) {
        this.repo = repo;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if(modelClass.isAssignableFrom(BoxViewModel.class)) {
            return (T) new BoxViewModel(repo);
        } else if(modelClass.isAssignableFrom(CameraViewModel.class)) {
            return (T) new CameraViewModel();
        } else {
            throw new IllegalArgumentException("ViewModel could not be loaded");
        }
    }
}

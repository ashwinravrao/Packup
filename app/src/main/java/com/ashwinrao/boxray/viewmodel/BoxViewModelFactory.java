package com.ashwinrao.boxray.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ashwinrao.boxray.data.BoxRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BoxViewModelFactory implements ViewModelProvider.Factory {

    private final BoxRepository repo;

    @Inject
    public BoxViewModelFactory(@NonNull BoxRepository repo) {
        this.repo = repo;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new BoxViewModel(repo);
    }
}

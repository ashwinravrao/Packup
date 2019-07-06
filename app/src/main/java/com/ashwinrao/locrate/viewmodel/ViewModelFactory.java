package com.ashwinrao.locrate.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ashwinrao.locrate.data.repo.BoxRepository;
import com.ashwinrao.locrate.data.repo.ItemRepository;
import com.ashwinrao.locrate.data.repo.MoveRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final MoveRepository moveRepo;
    private final BoxRepository boxRepo;
    private final ItemRepository itemRepo;

    @Inject
    public ViewModelFactory(@NonNull MoveRepository moveRepo, @NonNull BoxRepository boxRepo, @NonNull ItemRepository itemRepo) {
        this.moveRepo = moveRepo;
        this.boxRepo = boxRepo;
        this.itemRepo = itemRepo;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(MoveViewModel.class)) {
            return (T) new MoveViewModel(moveRepo);
        } else if (modelClass.isAssignableFrom(BoxViewModel.class)) {
            return (T) new BoxViewModel(boxRepo);
        } else if (modelClass.isAssignableFrom(PhotoViewModel.class)) {
            return (T) new PhotoViewModel();
        } else
            throw new IllegalArgumentException("ViewModel could not be loaded");
    }
}

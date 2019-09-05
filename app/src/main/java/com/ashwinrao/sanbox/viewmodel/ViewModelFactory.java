package com.ashwinrao.sanbox.viewmodel;


import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.ashwinrao.sanbox.data.repo.BoxRepository;
import com.ashwinrao.sanbox.data.repo.ItemRepository;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ViewModelFactory implements ViewModelProvider.Factory {

    private final BoxRepository boxRepo;
    private final ItemRepository itemRepo;

    @Inject
    public ViewModelFactory(@NonNull BoxRepository boxRepo, @NonNull ItemRepository itemRepo) {
        this.boxRepo = boxRepo;
        this.itemRepo = itemRepo;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(BoxViewModel.class)) {
            return (T) new BoxViewModel(boxRepo);
        } else if (modelClass.isAssignableFrom(CategoryViewModel.class)) {
            return (T) new CategoryViewModel();
        } else if (modelClass.isAssignableFrom(ItemViewModel.class)) {
            return (T) new ItemViewModel(itemRepo);
        } else
            throw new IllegalArgumentException("ViewModel could not be loaded");
    }
}

package com.ashwinrao.packup.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.ashwinrao.packup.data.model.Box;
import com.ashwinrao.packup.data.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel extends ViewModel {

    private List<String> cachedBoxCategories = new ArrayList<>();

    public void setCachedBoxCategories(@NonNull final List<Box> cachedBoxes) {
        cachedBoxCategories = new ArrayList<>();
        for (final Box box : cachedBoxes) {
            cachedBoxCategories.addAll(box.getCategories());
        }
    }

    public List<String> getCachedBoxCategories() {
        return cachedBoxCategories;
    }

}

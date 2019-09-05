package com.ashwinrao.sanbox.viewmodel;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.ashwinrao.sanbox.data.model.Box;
import com.ashwinrao.sanbox.data.model.Item;

import java.util.ArrayList;
import java.util.List;

public class CategoryViewModel extends ViewModel {

    private List<String> cachedBoxCategories = new ArrayList<>();
    private List<String> cachedItemCategories = new ArrayList<>();

    public void setCachedBoxCategories(@NonNull final List<Box> cachedBoxes) {
        cachedBoxCategories = new ArrayList<>();
        for (final Box box : cachedBoxes) {
            cachedBoxCategories.addAll(box.getCategories());
        }
    }

    public List<String> getCachedBoxCategories() {
        return cachedBoxCategories;
    }


    public void setCachedItemCategories(@NonNull final List<Item> cachedItems) {
        cachedItemCategories = new ArrayList<>();
        for (final Item item : cachedItems) {
            cachedItemCategories.add(item.getCategory());
        }
    }

    public List<String> getCachedItemCategories() {
        return cachedItemCategories;
    }

}

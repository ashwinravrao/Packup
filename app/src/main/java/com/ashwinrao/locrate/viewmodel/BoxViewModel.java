package com.ashwinrao.locrate.viewmodel;


import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.repo.BoxRepository;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class BoxViewModel extends ViewModel {

    private Box box = new Box();
    private final BoxRepository repo;

    BoxViewModel(BoxRepository repo) {
        this.repo = repo;
    }

    public Box getBox() {
        return this.box;
    }

    public LiveData<Box> getBoxByNumber(int id) {
        return repo.getBoxByNumber(id);
    }

    public LiveData<Box> getBoxByUUID(String uuid) {
        return repo.getBoxByUUID(uuid);
    }

    public LiveData<List<Box>> getBoxes() {
        return repo.getBoxes();
    }

    public boolean saveBox(@NonNull List<String> categories) {
        if (box.getName() != null) {
            box.setCategories(categories);
            repo.insert(this.box);
            return true;
        } else {
            return false;
        }
    }

    public LiveData<List<String>> getAllBoxCategories() {
        return Transformations.switchMap(repo.getBoxes(), boxes -> {
            final MutableLiveData<List<String>> mld = new MutableLiveData<>();
            List<String> categories = null;
            if (boxes != null) {
                categories = new ArrayList<>();
                for (final Box box : boxes) categories.addAll(box.getCategories());
            }
            mld.setValue(categories);
            return mld;
        });
    }

    public LiveData<Integer> getLastUsedBoxNumber() {
        return Transformations.switchMap(repo.getBoxes(), boxes -> {
            final MutableLiveData<Integer> mld = new MutableLiveData<>();
            int lastUsedNumber = 0;
            if(boxes != null) {
                if(boxes.size() > 0) {
                    lastUsedNumber = boxes.get(0).getNumber();
                }
            }
            mld.setValue(lastUsedNumber);
            return mld;
        });
    }

    public void delete(Box box) {
        repo.delete(box);
    }

    public void deleteMultiple(List<Box> boxes) {
        repo.delete(boxes.toArray(new Box[0]));
    }
}

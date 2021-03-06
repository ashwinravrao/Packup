package com.ashwinrao.packup.viewmodel;


import com.ashwinrao.packup.data.model.Box;
import com.ashwinrao.packup.data.repo.BoxRepository;

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
    private List<Box> cachedBoxes = new ArrayList<>();

    BoxViewModel(BoxRepository repo) {
        this.repo = repo;
    }

    public Box getBox() {
        return this.box;
    }

    public LiveData<Box> getBoxByUUID(String uuid) {
        return repo.getBoxByUUID(uuid);
    }

    public LiveData<List<Box>> getBoxes() {
        return repo.getBoxes();
    }

    public void setCachedBoxes(@NonNull final List<Box> cachedBoxes) {
        if (cachedBoxes.size() > 0) {
            this.cachedBoxes = cachedBoxes;
        } else {
            this.cachedBoxes.clear();
        }
    }

    public List<Box> getCachedBoxes() {
        return this.cachedBoxes;
    }

    public void updateBox(@NonNull Box box) {
        repo.update(box);
    }

    public LiveData<Integer> getLastUsedBoxNumber() {
        return Transformations.switchMap(repo.getBoxes(), boxes -> {
            final MutableLiveData<Integer> mld = new MutableLiveData<>();
            int lastUsedNumber = 0;
            if (boxes != null) {
                if (boxes.size() > 0) {
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

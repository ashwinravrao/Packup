package com.ashwinrao.boxray.viewmodel;

import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.data.BoxRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BoxViewModel extends ViewModel {

    private Box box = new Box();
    private final BoxRepository repo;
    private MutableLiveData<Boolean> shouldGoToInitialPage = new MutableLiveData<>();
    private MutableLiveData<Boolean> isAddComplete = new MutableLiveData<>();

    public BoxViewModel(@NonNull BoxRepository repo) {
        this.repo = repo;
    }

//    public void createBox() {
//        box = new Box();
//    }

    public Box getCurrentBox() {
        return box;
    }

    public LiveData<List<Box>> getBoxes() {
        return repo.getBoxes();
    }

    public LiveData<Box> getBoxByID(int id) {
        return repo.getBoxByID(id);
    }

    public LiveData<Boolean> getShouldGoToInitialPage() {
        return shouldGoToInitialPage;
    }

    public LiveData<Boolean> getIsAddComplete() {
        return isAddComplete;
    }

    public void verifySaveRequirements() {
        if (box.getName() != null) {
            repo.saveBox(box);
            isAddComplete.setValue(true);
        } else {
            shouldGoToInitialPage.setValue(true);
        }
    }
}

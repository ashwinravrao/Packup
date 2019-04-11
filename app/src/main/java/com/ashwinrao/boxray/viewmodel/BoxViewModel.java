package com.ashwinrao.boxray.viewmodel;

import android.app.Application;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.data.BoxRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BoxViewModel extends ViewModel {

    private final BoxRepository repository;

    private Box box;

    private MutableLiveData<Boolean> shouldGoToInitialPage;
    private MutableLiveData<Boolean> isAddComplete;

    public BoxViewModel(@NonNull Application application) {
        repository = ((Boxray) application).getRepository();
        shouldGoToInitialPage = new MutableLiveData<>();
        isAddComplete = new MutableLiveData<>();
        recreateBox();
    }

    public void recreateBox() {
        box = new Box();
    }

    public Box getBox() {
        return box;
    }

    public LiveData<List<Box>> getBoxes() {
        return repository.getBoxes();
    }

    public LiveData<Box> getBoxByID(int id) {
        return repository.getBoxByID(id);
    }

    public LiveData<Boolean> getShouldGoToInitialPage() {
        return shouldGoToInitialPage;
    }

    public LiveData<Boolean> getIsAddComplete() {
        return isAddComplete;
    }

    public void verifySaveRequirements() {
        if(box.getName() != null) {
            repository.saveBox(box);
            isAddComplete.setValue(true);
        } else {
            shouldGoToInitialPage.setValue(true);
        }
    }

    public void setIsAddComplete(boolean isAddComplete) {
        this.isAddComplete.setValue(isAddComplete);
    }

    public void setShouldGoToInitialPage(boolean shouldGoToInitialPage) {
        this.shouldGoToInitialPage.setValue(shouldGoToInitialPage);
    }
}

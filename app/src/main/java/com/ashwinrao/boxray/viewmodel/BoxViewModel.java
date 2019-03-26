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

    private Box box;
    private Boolean saveFlag = false;
    private MutableLiveData<Boolean> fieldsSatisfied;

    private final BoxRepository repository;

    public BoxViewModel(@NonNull Application application) {
        repository = ((Boxray) application).getRepository();
        fieldsSatisfied = new MutableLiveData<>();
        box = new Box();
    }

    public void setFieldsSatisfied(boolean areFieldsSatisfied) {
        saveFlag = areFieldsSatisfied;
        fieldsSatisfied.setValue(areFieldsSatisfied);
    }

    public LiveData<Boolean> getFieldsSatisfied() {
        return fieldsSatisfied;
    }

    public LiveData<List<Box>> getBoxes() {
        return repository.getBoxes();
    }

    public LiveData<Box> getBoxByID(int id) {
        return repository.getBoxByID(id);
    }

    public void save(Box box) {
        repository.saveBox(box);
    }


    // Incrementally builds box object using field input
    public void boxSetID(int id) { box.setId(id); }

    public void boxSetName(String name) { box.setName(name); }

    public void boxSetSource(String source) { box.setSource(source); }

    public void boxSetDestination(String destination) { box.setDestination(destination); }

    public void boxSetNotes(String notes) { box.setNotes(notes); }

    public void boxSetFavorite(boolean favorite) { box.setFavorite(favorite); }

    public void boxSetContents(List<String> contents) { box.setContents(contents); }

    public boolean boxSave() {
        if (saveFlag) {
            repository.saveBox(box);
            return true;    // for checking and presenting save confirmation to user
        } else {
            return false;
        }
    }
}

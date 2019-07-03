package com.ashwinrao.locrate.viewmodel;

import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.repo.BoxRepository;
import com.ashwinrao.locrate.util.BoxValidator;

import java.util.ArrayList;
import java.util.List;

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

    public LiveData<Box> getBoxByID(String id) {
        return repo.getBoxByID(id);
    }

    public LiveData<List<Box>> getBoxes() {
        return repo.getBoxes();
    }

    public boolean saveBox() {
        if(new BoxValidator(this.box).validate()) {
            repo.insert(this.box);
            return true;
        } else {
            return false;
        }
    }

    public LiveData<List<String>> getAllContents() {
        return Transformations.switchMap(getBoxes(), value -> {
            MutableLiveData<List<String>> mld = new MutableLiveData<>();
            List<String> contents = new ArrayList<>();
            for(Box box : value) contents.addAll(box.getContents());
            mld.setValue(contents);
            return mld;
        });
    }

    public boolean areChangesUnsaved() {
        return box.getName() != null || !box.getDescription().equals("No description") || box.getContents() != null;
    }

    public void delete(Box box) {
        repo.delete(box);
    }
}

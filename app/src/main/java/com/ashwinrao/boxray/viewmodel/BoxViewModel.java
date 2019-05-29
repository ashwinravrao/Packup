package com.ashwinrao.boxray.viewmodel;


import android.util.Log;

import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.data.BoxRepository;
import com.ashwinrao.boxray.util.BoxValidator;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BoxViewModel extends ViewModel {

    private Box box = new Box();
    private final BoxRepository repo;
    private List<String> paths = new ArrayList<>();
    private MutableLiveData<List<String>> pathsMLD = new MutableLiveData<>();

    private static final String TAG = "Boxray";

    BoxViewModel(BoxRepository repo) {
        this.repo = repo;
    }

    public Box getBox() {
        return this.box;
    }


    // Repository Methods

    public LiveData<Box> getBoxByID(int id) {
        return repo.getBoxByID(id);
    }

    public LiveData<List<Box>> getBoxes() {
        return repo.getBoxes();
    }

    public boolean saveBox() {
        if(new BoxValidator(this.box).validate()) {
            repo.saveBox(this.box);
            return true;
        } else {
            return false;
        }
    }

    public void addPath(String path) {
        paths.add(path);
        pathsMLD.setValue(paths);
        Log.d(TAG, "viewmodel addPath: " + path);
    }

    public LiveData<List<String>> getPaths() {
        return pathsMLD;
    }


    public void save(Box box) {
        repo.saveBox(box);
    }

    public void delete(Box box) {
        repo.delete(box);
    }
}

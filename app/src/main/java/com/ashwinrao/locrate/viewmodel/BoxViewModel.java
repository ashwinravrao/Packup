package com.ashwinrao.locrate.viewmodel;

import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.repo.BoxRepository;
import com.ashwinrao.locrate.view.adapter.BoxesAdapter;

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
    private BoxesAdapter boxesAdapter;

    BoxViewModel(BoxRepository repo) {
        this.repo = repo;
    }

    public Box getBox() {
        return this.box;
    }

    public LiveData<Box> getBoxByID(int id) {
        return repo.getBoxByID(id);
    }

    public LiveData<List<Box>> getBoxes() {
        return repo.getBoxes();
    }

    public boolean saveBox() {
        if(box.getName() != null) {
            repo.insert(this.box);
            return true;
        } else {
            return false;
        }
    }

    public void setBoxesAdapter(@NonNull BoxesAdapter boxesAdapter) {
        this.boxesAdapter = boxesAdapter;
    }

    public BoxesAdapter getBoxesAdapter() {
        return this.boxesAdapter;
    }
//    public LiveData<List<String>> getAllContents() {
//        return Transformations.switchMap(getBoxes(), value -> {
//            MutableLiveData<List<String>> mld = new MutableLiveData<>();
//            List<String> contents = new ArrayList<>();
//            for(Box box : value) contents.addAll(box.getContents());
//            mld.setValue(contents);
//            return mld;
//        });
//    }

    public void delete(Box box) {
        repo.delete(box);
    }
}

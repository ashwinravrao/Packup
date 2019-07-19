package com.ashwinrao.locrate.viewmodel;

import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.repo.BoxRepository;
import com.ashwinrao.locrate.view.adapter.BoxesAdapter;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

public class BoxViewModel extends ViewModel {

    private Box box = new Box();
    private final BoxRepository repo;
    private MutableLiveData<Boolean> nfcOverwriteMLD = new MutableLiveData<>(false);
    private BoxesAdapter boxesAdapter;

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

    public boolean saveBox() {
        if (box.getName() != null) {
            repo.insert(this.box);
            return true;
        } else {
            return false;
        }
    }

    public LiveData<Boolean> getNfcOverwritePermission() {
        return nfcOverwriteMLD;
    }

    public void setNfcOverwritePermission(boolean isOverwritePermitted) {
        nfcOverwriteMLD.setValue(isOverwritePermitted);
    }

    public void setBoxesAdapter(@NonNull BoxesAdapter boxesAdapter) {
        this.boxesAdapter = boxesAdapter;
    }

    public BoxesAdapter getBoxesAdapter() {
        return this.boxesAdapter;
    }

    public void delete(Box box) {
        repo.delete(box);
    }

    public void deleteMultiple(List<Box> boxes) {
        repo.delete(boxes.toArray(new Box[0]));
    }
}

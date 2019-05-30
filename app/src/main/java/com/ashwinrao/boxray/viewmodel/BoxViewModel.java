package com.ashwinrao.boxray.viewmodel;

import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.data.BoxRepository;
import com.ashwinrao.boxray.util.BoxValidator;

import java.util.List;

import androidx.lifecycle.LiveData;
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

    public void save(Box box) {
        repo.saveBox(box);
    }

    public void delete(Box box) {
        repo.delete(box);
    }
}

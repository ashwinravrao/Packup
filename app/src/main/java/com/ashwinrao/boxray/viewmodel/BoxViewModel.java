package com.ashwinrao.boxray.viewmodel;


import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.data.BoxRepository;
import com.ashwinrao.boxray.util.AddBoxCompletionListener;
import com.ashwinrao.boxray.util.InputValidator;
import com.ashwinrao.boxray.util.Utilities;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class BoxViewModel extends ViewModel {

    private Box box = new Box();
    private final BoxRepository repo;
    private AddBoxCompletionListener listener;

    BoxViewModel(BoxRepository repo) {
        this.repo = repo;
    }

    public void setCompletionListener(AddBoxCompletionListener listener) {
        this.listener = listener;
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

    public void saveBox() {
        if(new InputValidator(this.box).validate()) {
            repo.saveBox(this.box);
            listener.finishParentActivity(true);
        } else {
            listener.returnToFirstPage(true);
        }
    }
}

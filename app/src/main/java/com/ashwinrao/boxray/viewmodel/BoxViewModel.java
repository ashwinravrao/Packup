package com.ashwinrao.boxray.viewmodel;


import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.data.BoxRepository;
import com.ashwinrao.boxray.util.AddBoxCompletionListener;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class BoxViewModel extends ViewModel {

    private Box box = new Box();
    private final BoxRepository repo;
    private AddBoxCompletionListener listener;

    public BoxViewModel(@NonNull BoxRepository repo) {
        this.repo = repo;
    }

    public void setCompletionListener(AddBoxCompletionListener listener) {
        this.listener = listener;
    }

    public Box getCurrentBox() {
        return box;
    }

    public LiveData<List<Box>> getBoxes() {
        return repo.getBoxes();
    }

    public LiveData<Box> getBoxByID(int id) {
        return repo.getBoxByID(id);
    }

    public void verifySaveRequirements() {
        if (box.getName() != null) {
            repo.saveBox(box);
            listener.finishParentActivity(true);
        } else {
            listener.returnToFirstPage(true);
        }
    }
}

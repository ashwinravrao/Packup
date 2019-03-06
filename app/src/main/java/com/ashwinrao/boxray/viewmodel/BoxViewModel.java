package com.ashwinrao.boxray.viewmodel;

import android.app.Application;

import com.ashwinrao.boxray.Boxray;
import com.ashwinrao.boxray.data.Box;
import com.ashwinrao.boxray.data.BoxRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class BoxViewModel extends ViewModel {

    private final BoxRepository mRepository;

    public BoxViewModel(@NonNull Application application) {
        mRepository = ((Boxray) application).getRepository();
    }

    public LiveData<List<Box>> getBoxes() {
        return mRepository.getBoxes();
    }

    public LiveData<Box> getBoxByID(int id) { return mRepository.getBoxByID(id); }

    public void save(Box box) {
        mRepository.saveBox(box);
    }

}

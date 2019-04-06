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
    private MutableLiveData<String> toolbarTitle;
    private MutableLiveData<Boolean> canViewPagerAdvance;
    private LiveData<Boolean> wasSwiped;

    public BoxViewModel(@NonNull Application application) {
        repository = ((Boxray) application).getRepository();
        toolbarTitle = new MutableLiveData<>();
        canViewPagerAdvance = new MutableLiveData<>();
    }

    public void setWasSwiped(LiveData<Boolean> wasSwiped) {
        this.wasSwiped = wasSwiped;
    }

    public LiveData<Boolean> getWasSwiped() {
        return this.wasSwiped;
    }

    public void setCanViewPagerAdvance(boolean canViewPagerAdvance) {
        this.canViewPagerAdvance.setValue(canViewPagerAdvance);
    }

    public LiveData<Boolean> getCanViewPagerAdvance() {
        return this.canViewPagerAdvance;
    }

    public void setToolbarTitle(String title) {
        toolbarTitle.setValue(title);
    }

    public LiveData<String> getToolbarTitle() {
        return toolbarTitle;
    }

    public LiveData<List<Box>> getBoxes() {
        return repository.getBoxes();
    }

    public LiveData<Box> getBoxByID(int id) {
        return repository.getBoxByID(id);
    }
}

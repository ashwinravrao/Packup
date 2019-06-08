package com.ashwinrao.boxray.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class CameraViewModel extends ViewModel {

    private MutableLiveData<List<String>> imagePaths = new MutableLiveData<>();

    public void setImagePaths(List<String> imagePaths) {
        this.imagePaths.setValue(imagePaths);
    }

    public LiveData<List<String>> getImagePaths() {
        return imagePaths;
    }

}

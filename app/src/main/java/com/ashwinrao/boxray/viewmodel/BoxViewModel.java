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

//    private Box mBoxBuilder;
//    private int mBoxBuilderFieldTally = 0;

    public String[] boxArgs = new String[7];
    private MutableLiveData<Boolean> mFieldsSatisfied;

    private final BoxRepository mRepository;

    public BoxViewModel(@NonNull Application application) {
        mRepository = ((Boxray) application).getRepository();
        mFieldsSatisfied = new MutableLiveData<>();
//        mBoxBuilder = new Box();
    }

    public void setFieldsSatisfied(boolean areFieldsSatisfied) {
        mFieldsSatisfied.setValue(areFieldsSatisfied);
    }

    public LiveData<Boolean> getFieldsSatisfied() {
        return mFieldsSatisfied;
    }

    public LiveData<List<Box>> getBoxes() {
        return mRepository.getBoxes();
    }

    public LiveData<Box> getBoxByID(int id) {
        return mRepository.getBoxByID(id);
    }

    public void save(Box box) {
        mRepository.saveBox(box);
    }

//    public void addBoxBuilderId(int id) {
//        mBoxBuilder.setId(id);
//        mBoxBuilderFieldTally++;
//    }
//
//    public void addBoxBuilderName(String name) {
//        mBoxBuilder.setName(name);
//        mBoxBuilderFieldTally++;
//    }
//
//    public void addBoxBuilderSource(String source) {
//        mBoxBuilder.setSource(source);
//        mBoxBuilderFieldTally++;
//    }
//
//    public void addBoxBuilderDestination(String destination) {
//        mBoxBuilder.setDestination(destination);
//        mBoxBuilderFieldTally++;
//    }
//
//    public void addBoxBuilderNotes(String notes) {
//        mBoxBuilder.setNotes(notes);
//        mBoxBuilderFieldTally++;
//    }
//
//    public void addBoxBuilderContents(List<String> contents) {
//        mBoxBuilder.setContents(contents);
//        mBoxBuilderFieldTally++;
//    }
//
//    public void addBoxBuilderFavorite(boolean isFavorite) {
//        mBoxBuilder.setFavorite(isFavorite);
//        mBoxBuilderFieldTally++;
//    }
//
//    public boolean saveBuiltBox() {
//        if (mBoxBuilderFieldTally == 7) {
//            mRepository.saveBox(mBoxBuilder);
//            return true;
//        } else {
//            return false;
//        }
//    }
}

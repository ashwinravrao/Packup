package com.ashwinrao.boxray.data;

import android.util.Log;

import com.ashwinrao.boxray.util.AsyncTasks;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import androidx.lifecycle.LiveData;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BoxRepository {

    private static final String TAG = "BoxRepository";

    private BoxDao dao;

    @Inject
    public BoxRepository(BoxDatabase database) {
        dao = database.dao();
    }

    public LiveData<List<Box>> getBoxes() {
        return dao.getBoxes();
    }

    public LiveData<Box> getBoxByID(int id) { return dao.getBoxById(id); }

    public void saveBox(Box box) {
        new AsyncTasks.Insert(dao).execute(box);
    }

    public void delete(Box box) {
        new AsyncTasks.Delete(dao).execute(box);
    }
}

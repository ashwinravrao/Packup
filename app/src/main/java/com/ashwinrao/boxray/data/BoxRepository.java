package com.ashwinrao.boxray.data;

import com.ashwinrao.boxray.util.AsyncTasks;

import java.util.List;

import androidx.lifecycle.LiveData;

public class BoxRepository {

    private BoxDao dao;
    private static BoxRepository instance;
//    private BoxDatabase database;

    public static BoxRepository getInstance(BoxDatabase db) {
        if(instance == null) {
            instance = new BoxRepository(db);
        }
        return instance;
    }

    private BoxRepository(BoxDatabase db) {
        dao = db.mDao();
    }

    public LiveData<List<Box>> getBoxes() {
        return dao.getBoxes();
    }

    public LiveData<Box> getBoxByID(int id) { return dao.getBoxById(id); }

    public void saveBox(Box box) {
        new AsyncTasks.Insert(dao).execute(box);
    }
}

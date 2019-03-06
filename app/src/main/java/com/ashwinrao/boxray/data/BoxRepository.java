package com.ashwinrao.boxray.data;

import com.ashwinrao.boxray.util.AsyncTasks;

import java.util.List;

import androidx.lifecycle.LiveData;

public class BoxRepository {

    private BoxDao mDao;
    private static BoxRepository sInstance;
    private BoxDatabase mDatabase;

    public static BoxRepository getInstance(BoxDatabase db) {
        if(sInstance == null) {
            sInstance = new BoxRepository(db);
        }
        return sInstance;
    }

    private BoxRepository(BoxDatabase db) {
        mDao = db.mDao();
    }

    public LiveData<List<Box>> getBoxes() {
        return mDao.getBoxes();
    }

    public LiveData<Box> getBoxByID(int id) { return mDao.getBoxById(id); }

    public void saveBox(Box box) {
        new AsyncTasks.Insert(mDao).execute(box);
    }
}

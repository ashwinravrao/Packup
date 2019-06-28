package com.ashwinrao.locrate.data;

import android.os.AsyncTask;
import android.util.Log;

import com.ashwinrao.locrate.util.AsyncTasks;

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

    public List<Box> listBoxes() {
        try {
            return new AsyncTasks.ListBoxes(dao).execute().get();
        } catch (ExecutionException e) {
            Log.e(TAG, "listBoxes: " + e.getMessage());
        } catch (InterruptedException e) {
            Log.e(TAG, "listBoxes: " + e.getMessage());
        }
        return null;
    }

    public void saveBox(Box box) {
        new AsyncTasks.Insert(dao).execute(box);
    }

    public void delete(Box box) {
        new AsyncTasks.Delete(dao).execute(box);
    }
}

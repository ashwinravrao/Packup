package com.ashwinrao.locrate.data.repo;

import com.ashwinrao.locrate.data.AppDatabase;
import com.ashwinrao.locrate.data.repo.dao.BoxDao;
import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.AsyncTasks;

import java.util.List;

import androidx.lifecycle.LiveData;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class BoxRepository {

    private BoxDao dao;

    @Inject
    public BoxRepository(AppDatabase database) {
        dao = database.boxDao();
    }

    public LiveData<List<Box>> getBoxes() {
        return dao.getBoxes();
    }

    public LiveData<Box> getBoxByID(String id) { return dao.getBoxById(id); }

    public void insert(Box... boxes) {
        new AsyncTasks.BoxAsyncTask(dao, "insert").execute(boxes);
    }

    public void update(Box... boxes) {
        new AsyncTasks.BoxAsyncTask(dao, "update").execute(boxes);
    }

    public void delete(Box... boxes) {
        new AsyncTasks.BoxAsyncTask(dao, "delete").execute(boxes);
    }
}

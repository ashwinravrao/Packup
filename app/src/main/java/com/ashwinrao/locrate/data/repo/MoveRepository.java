package com.ashwinrao.locrate.data.repo;

import androidx.lifecycle.LiveData;

import com.ashwinrao.locrate.data.AppDatabase;
import com.ashwinrao.locrate.data.repo.dao.MoveDao;
import com.ashwinrao.locrate.data.model.Move;
import com.ashwinrao.locrate.data.AsyncTasks;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MoveRepository {

    private MoveDao dao;

    @Inject
    public MoveRepository(AppDatabase database) {
        dao = database.moveDao();
    }

    public LiveData<List<Move>> getMoves() {
        return dao.getMoves();
    }

    public void insert(Move... moves) {
        new AsyncTasks.MoveAsyncTask(dao, "insert").execute(moves);
    }

    public void update(Move... moves) {
        new AsyncTasks.MoveAsyncTask(dao, "update").execute(moves);
    }

    public void delete(Move... moves) {
        new AsyncTasks.MoveAsyncTask(dao, "delete").execute(moves);
    }

}

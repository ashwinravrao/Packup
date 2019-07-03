package com.ashwinrao.locrate.data;

import com.ashwinrao.locrate.util.AsyncTasks;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MoveRepository {

    private MoveDao dao;

    @Inject
    public MoveRepository(AppDatabase database) {
        dao = database.moveDao();
    }

    public void insert(Move... moves) {
        new AsyncTasks.InsertMove(dao).execute(moves);
    }

    public void update(Move... moves) {
        new AsyncTasks.UpdateMove(dao).execute(moves);
    }

    public void delete(Move... moves) {
        new AsyncTasks.DeleteMove(dao).execute(moves);
    }

}

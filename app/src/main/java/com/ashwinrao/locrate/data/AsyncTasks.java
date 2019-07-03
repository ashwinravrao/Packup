package com.ashwinrao.locrate.data;

import android.os.AsyncTask;

import androidx.room.Delete;

import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.model.Item;
import com.ashwinrao.locrate.data.repo.dao.BoxDao;
import com.ashwinrao.locrate.data.model.Move;
import com.ashwinrao.locrate.data.repo.dao.ItemDao;
import com.ashwinrao.locrate.data.repo.dao.MoveDao;

public class AsyncTasks {

    public static class BoxAsyncTask extends AsyncTask<Box, Void, Void> {

        private BoxDao dao;
        private String operation;

        public BoxAsyncTask(BoxDao dao, String operation) {
            this.dao = dao;
            this.operation = operation;
        }

        @Override
        protected Void doInBackground(Box... boxes) {
            switch (operation.toLowerCase()) {
                case "insert":
                    dao.insert(boxes);
                    return null;
                case "update":
                    dao.update(boxes);
                    return null;
                case "delete":
                    dao.delete(boxes);
                    return null;
                default:
                    return null;
            }
        }
    }

    public static class MoveAsyncTask extends AsyncTask<Move, Void, Void> {

        private MoveDao dao;
        private String operation;

        public MoveAsyncTask(MoveDao dao, String operation) {
            this.dao = dao;
            this.operation = operation;
        }

        @Override
        protected Void doInBackground(Move... moves) {
            switch (operation.toLowerCase()) {
                case "insert":
                    dao.insert(moves);
                    return null;
                case "update":
                    dao.update(moves);
                    return null;
                case "delete":
                    dao.delete(moves);
                    return null;
                default:
                    return null;
            }
        }
    }

    public static class ItemAsyncTask extends AsyncTask<Item, Void, Void> {

        private ItemDao dao;
        private String operation;

        public ItemAsyncTask(ItemDao dao, String operation) {
            this.dao = dao;
            this.operation = operation;
        }

        @Override
        protected Void doInBackground(Item... items) {
            switch (operation.toLowerCase()) {
                case "insert":
                    dao.insert(items);
                    return null;
                case "update":
                    dao.update(items);
                    return null;
                case "delete":
                    dao.delete(items);
                    return null;
                default:
                    return null;
            }
        }
    }

}

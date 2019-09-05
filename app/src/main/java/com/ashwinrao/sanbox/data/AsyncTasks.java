package com.ashwinrao.sanbox.data;

import android.os.AsyncTask;

import com.ashwinrao.sanbox.data.model.Box;
import com.ashwinrao.sanbox.data.model.Item;
import com.ashwinrao.sanbox.data.repo.dao.BoxDao;
import com.ashwinrao.sanbox.data.repo.dao.ItemDao;

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

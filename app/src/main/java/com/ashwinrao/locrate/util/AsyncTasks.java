package com.ashwinrao.locrate.util;

import android.os.AsyncTask;

import com.ashwinrao.locrate.data.Box;
import com.ashwinrao.locrate.data.BoxDao;

public class AsyncTasks {

    public static class Insert extends AsyncTask<Box, Void, Void> {

        private BoxDao dao;

        public Insert(BoxDao dao) { this.dao = dao; }

        @Override
        protected Void doInBackground(Box... boxes) {
            dao.save(boxes[0]);
            return null;
        }
    }

    public static class Delete extends AsyncTask<Box, Void, Void> {

        private BoxDao dao;

        public Delete(BoxDao dao) { this.dao = dao; }

        @Override
        protected Void doInBackground(Box... boxes) {
            dao.delete(boxes[0]);
            return null;
        }
    }

}

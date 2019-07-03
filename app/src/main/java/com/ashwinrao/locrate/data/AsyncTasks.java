package com.ashwinrao.locrate.data;

import android.os.AsyncTask;

import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.repo.dao.BoxDao;
import com.ashwinrao.locrate.data.model.Move;
import com.ashwinrao.locrate.data.repo.dao.MoveDao;

public class AsyncTasks {

    public static class InsertBox extends AsyncTask<Box, Void, Void> {

        private BoxDao dao;

        public InsertBox(BoxDao dao) { this.dao = dao; }

        @Override
        protected Void doInBackground(Box... boxes) {
            dao.insert(boxes[0]);
            return null;
        }
    }

    public static class UpdateBox extends AsyncTask<Box, Void, Void> {

        private BoxDao dao;

        public UpdateBox(BoxDao dao) { this.dao = dao; }

        @Override
        protected Void doInBackground(Box... boxes) {
            dao.update(boxes[0]);
            return null;
        }
    }

    public static class DeleteBox extends AsyncTask<Box, Void, Void> {

        private BoxDao dao;

        public DeleteBox(BoxDao dao) { this.dao = dao; }

        @Override
        protected Void doInBackground(Box... boxes) {
            dao.delete(boxes[0]);
            return null;
        }
    }

    public static class InsertMove extends AsyncTask<Move, Void, Void> {

        private MoveDao dao;

        public InsertMove(MoveDao dao) { this.dao = dao; }

        @Override
        protected Void doInBackground(Move... moves) {
            dao.insert(moves[0]);
            return null;
        }
    }

    public static class UpdateMove extends AsyncTask<Move, Void, Void> {

        private MoveDao dao;

        public UpdateMove(MoveDao dao) { this.dao = dao; }

        @Override
        protected Void doInBackground(Move... moves) {
            dao.update(moves[0]);
            return null;
        }
    }

    public static class DeleteMove extends AsyncTask<Move, Void, Void> {

        private MoveDao dao;

        public DeleteMove(MoveDao dao) { this.dao = dao; }

        @Override
        protected Void doInBackground(Move... moves) {
            dao.delete(moves[0]);
            return null;
        }
    }

}

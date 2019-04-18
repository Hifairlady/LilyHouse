package com.example.lilyhouse.repositories;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.lilyhouse.daos.MangaCoverItemDao;
import com.example.lilyhouse.database.LilyRoomDatabase;
import com.example.lilyhouse.models.MangaCoverItem;

import java.util.List;

public class CoverItemRepo {
    private MangaCoverItemDao coverItemDao;
    private LiveData<List<MangaCoverItem>> mCoverItems;

    public CoverItemRepo(Application application) {
        LilyRoomDatabase database = LilyRoomDatabase.getInstance(application);
        coverItemDao = database.coverItemDao();
        mCoverItems = coverItemDao.getAllItems();
    }

    public LiveData<List<MangaCoverItem>> getCoverItems() {
        return mCoverItems;
    }

    public void insertItems(MangaCoverItem... items) {
        new InsertAsyncTask(coverItemDao).execute(items);
    }

    public void deleteAllItems() {
        new DeleteAllAsyncTask(coverItemDao).execute();
    }

    private class InsertAsyncTask extends AsyncTask<MangaCoverItem, Void, Void> {

        private MangaCoverItemDao itemDao;

        public InsertAsyncTask(MangaCoverItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(MangaCoverItem... mangaCoverItems) {
            itemDao.insertItems(mangaCoverItems);
            return null;
        }
    }

    private class DeleteAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private MangaCoverItemDao itemDao;

        public DeleteAllAsyncTask(MangaCoverItemDao itemDao) {
            this.itemDao = itemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            itemDao.deleteAllItems();
            return null;
        }
    }
}

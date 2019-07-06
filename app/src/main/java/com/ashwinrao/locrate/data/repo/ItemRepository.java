package com.ashwinrao.locrate.data.repo;

import androidx.lifecycle.LiveData;

import com.ashwinrao.locrate.data.AppDatabase;
import com.ashwinrao.locrate.data.AsyncTasks;
import com.ashwinrao.locrate.data.model.Item;
import com.ashwinrao.locrate.data.repo.dao.ItemDao;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class ItemRepository {

    private ItemDao dao;

    @Inject
    public ItemRepository(AppDatabase database) {
        dao = database.itemDao();
    }

    public LiveData<List<Item>> getItems() {
        return dao.getItems();
    }

    public LiveData<Item> getItem(String name) {
        return dao.getItem(name);
    }

    public LiveData<List<Item>> getItemsValued(double estimatedValue) {
        return dao.getItemsValued(estimatedValue);
    }

    public void insert(Item... items) {
        new AsyncTasks.ItemAsyncTask(dao, "insert").execute(items);
    }

    public void update(Item... items) {
        new AsyncTasks.ItemAsyncTask(dao, "update").execute(items);
    }

    public void delete(Item... items) {
        new AsyncTasks.ItemAsyncTask(dao, "delete").execute(items);
    }




}
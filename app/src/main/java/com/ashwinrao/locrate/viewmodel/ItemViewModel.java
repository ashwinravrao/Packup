package com.ashwinrao.locrate.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ashwinrao.locrate.data.model.Item;
import com.ashwinrao.locrate.data.repo.ItemRepository;

import java.util.ArrayList;
import java.util.List;

public class ItemViewModel extends ViewModel {

    private final ItemRepository repo;
    private List<Item> items = new ArrayList<>();

    ItemViewModel(ItemRepository repo) {
        this.repo = repo;
    }

    public LiveData<List<Item>> getAllItemsFromDatabase() {
        return repo.getItems();
    }

    public LiveData<List<Item>> getItemsFromBox(String boxUUID) {
        return repo.getItemsFromBox(boxUUID);
    }

    public void clearItems() {
        this.items.clear();
    }

    public void setItems(List<Item> items) {
        this.items.addAll(items);
    }

    public List<Item> getItemsFromThis() {
        return this.items;
    }

    public void insertItems(List<Item> items) {
        repo.insert(items.toArray(new Item[0]));
    }

    public void updateItem(Item item) {
        repo.update(item);
    }

    public void deleteItems(List<Item> items) {
        repo.delete(items.toArray(new Item[0]));
    }

}

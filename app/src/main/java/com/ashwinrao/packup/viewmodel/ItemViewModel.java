package com.ashwinrao.packup.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.ashwinrao.packup.data.model.Item;
import com.ashwinrao.packup.data.repo.ItemRepository;

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

    public void clearItemsFromThis() {
        this.items.clear();
    }

    public void removeItemFromThis(Item item) {
        this.items.remove(item);
    }

    public void addItemToThis(Item item, int position) {
        this.items.add(position, item);
    }

    public void addItemsToThis(List<Item> items) {
        this.items.addAll(items);
    }

    public void setItemsToThis(List<Item> items) {
        this.items = items;
    }

    public List<Item> getItemsFromThis() {
        return this.items;
    }

    public void insertItems(List<Item> items) {
        repo.insert(items.toArray(new Item[0]));
    }

    public void insertItem(Item item) {
        repo.insert(item);
    }

    public void updateItem(Item item) {
        repo.update(item);
    }

    public void updateItems(List<Item> items) {
        repo.update(items.toArray(new Item[0]));
    }

    public void deleteItems(List<Item> items) {
        repo.delete(items.toArray(new Item[0]));
    }

    public void deleteItem(Item item) { repo.delete(item); }

}

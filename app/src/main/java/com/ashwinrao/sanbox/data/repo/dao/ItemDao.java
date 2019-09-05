package com.ashwinrao.sanbox.data.repo.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ashwinrao.sanbox.data.model.Item;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface ItemDao {

    @Query("select * from items order by packed_date desc")
    LiveData<List<Item>> getItems();

    @Query("select * from items where name = :name")
    LiveData<Item> getItem(String name);

    @Query("select * from items where box_uuid = :boxUUID")
    LiveData<List<Item>> getItemsFromBox(String boxUUID);

    @Query("select * from items where estimated_value >= :estimatedValue")
    LiveData<List<Item>> getItemsValued(double estimatedValue);

    @Insert(onConflict = REPLACE)
    void insert(Item... items);

    @Update(onConflict = REPLACE)
    void update(Item... items);

    @Delete
    void delete(Item... items);

}

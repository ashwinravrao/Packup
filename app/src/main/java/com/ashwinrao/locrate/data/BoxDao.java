package com.ashwinrao.locrate.data;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

@Dao
public interface BoxDao {

    /**
     * @return A collection of all boxes from {@link BoxDatabase}.
     */

    @Query("SELECT *, `rowid` FROM boxes ORDER BY created DESC")
    List<Box> listBoxes();

    /**
     * @return An observable collection of all boxes from {@link BoxDatabase}.
     */

    @Query("SELECT *, `rowid` FROM boxes ORDER BY created DESC")
    LiveData<List<Box>> getBoxes();


    /**
     * @return An observable box (with specified name field) from {@link BoxDatabase}.
     */

    @Query("SELECT *, `rowid` FROM boxes WHERE name = :name")
    LiveData<Box> getBoxByName(String name);


    /**
     * @return An observable box (with specified id field) from {@link BoxDatabase}.
     */

    @Query("SELECT *, `rowid` FROM boxes WHERE rowid = :id")
    LiveData<Box> getBoxById(int id);


    /**
     * Insert/save a box to the {@link BoxDatabase}.
     *
     * Overwrites boxes that have the same primary key.
     */

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void save(Box box);


    /**
     * Delete a box from {@link BoxDatabase}.
     */

    @Delete
    void delete(Box box);

}

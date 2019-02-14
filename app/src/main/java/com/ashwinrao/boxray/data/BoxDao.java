package com.ashwinrao.boxray.data;

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
     * @return An observable collection of all boxes from {@link BoxDatabase}.
     */

    @Query("SELECT * FROM boxes ORDER BY created DESC")
    LiveData<List<Box>> getBoxes();


    /**
     * @return An observable collection of boxes from {@link BoxDatabase}
     * that have been saved as favorites.
     */

    @Query("SELECT * FROM boxes WHERE favorite = 1")
    LiveData<List<Box>> getFavoriteBoxes();


    /**
     * @return An observable box (with specified name field) from {@link BoxDatabase}.
     */

    @Query("SELECT * FROM boxes WHERE name = :name")
    LiveData<Box> getBoxByName(String name);


    /**
     * @return An observable box (with specified id field) from {@link BoxDatabase}.
     */

    @Query("SELECT * FROM boxes WHERE id = :id")
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

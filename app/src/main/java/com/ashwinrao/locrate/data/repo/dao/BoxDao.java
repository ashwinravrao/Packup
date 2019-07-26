package com.ashwinrao.locrate.data.repo.dao;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ashwinrao.locrate.data.AppDatabase;
import com.ashwinrao.locrate.data.model.Box;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface BoxDao {

    /**
     * @return An observable collection of all boxes from {@link AppDatabase}.
     */

    @Query("SELECT * FROM boxes ORDER BY number DESC")
    LiveData<List<Box>> getBoxes();


    /**
     * @return An observable box (with specified name field) from {@link AppDatabase}.
     */

    @Query("SELECT * FROM boxes WHERE name = :name")
    LiveData<Box> getBoxByName(String name);


    /**
     * @return An observable box (with specified id field) from {@link AppDatabase}.
     */

    @Query("SELECT * FROM boxes WHERE number = :number")
    LiveData<Box> getBoxByNumber(int number);


    @Query("SELECT * FROM boxes WHERE id = :uuid")
    LiveData<Box> getBoxByUUID(String uuid);

    /**
     * InsertBox/save a box to the {@link AppDatabase}.
     *
     * Overwrites boxes that have the same primary key.
     */

    @Insert(onConflict = REPLACE)
    void insert(Box...boxes);

    @Update(onConflict = REPLACE)
    void update(Box...boxes);

    /**
     * DeleteBox a box from {@link AppDatabase}.
     */

    @Delete
    void delete(Box...boxes);
}

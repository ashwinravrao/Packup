package com.ashwinrao.locrate.data.repo.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.ashwinrao.locrate.data.model.Move;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MoveDao {

    @Insert(onConflict = REPLACE)
    void insert(Move...moves);

    @Update(onConflict = REPLACE)
    void update(Move...moves);

    @Delete
    void delete(Move...moves);
}

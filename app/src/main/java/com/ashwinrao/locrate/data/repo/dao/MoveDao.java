package com.ashwinrao.locrate.data.repo.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.ashwinrao.locrate.data.model.Move;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MoveDao {

    @Query("select * from moves order by id desc")
    LiveData<List<Move>> getMoves();

    @Insert(onConflict = REPLACE)
    void insert(Move...moves);

    @Update(onConflict = REPLACE)
    void update(Move...moves);

    @Delete
    void delete(Move...moves);
}

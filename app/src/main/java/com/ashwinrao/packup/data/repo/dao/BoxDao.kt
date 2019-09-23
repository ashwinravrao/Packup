package com.ashwinrao.packup.data.repo.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.ashwinrao.packup.data.model.Box

@Dao
interface BoxDao {

    @Query("select * from boxes order by number desc")
    fun getBoxes() : LiveData<List<Box>>

    @Query("select * from boxes where name = :name")
    fun getBoxByName(name: String) : LiveData<Box>

    @Query("select * from boxes where id = :uuid")
    fun getBoxByUUID(uuid: String) : LiveData<Box>

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg box: Box)

    @Update(onConflict = REPLACE)
    suspend fun update(vararg box: Box)

    @Delete
    suspend fun delete(vararg box: Box)
}

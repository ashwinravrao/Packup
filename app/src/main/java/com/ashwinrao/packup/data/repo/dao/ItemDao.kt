
package com.ashwinrao.packup.data.repo.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import com.ashwinrao.packup.data.model.Item

@Dao
interface ItemDao {

    @Query("select * from items order by packed_date desc")
    fun getItems() : LiveData<List<Item>>

    @Query("select * from items where box_uuid = :boxUUID")
    fun getItemsFromBox(boxUUID: String) : LiveData<List<Item>>

    @Insert(onConflict = REPLACE)
    suspend fun insert(vararg item : Item)

    @Update(onConflict = REPLACE)
    suspend fun update(vararg item : Item)

    @Delete
    suspend fun delete(vararg item : Item)

}

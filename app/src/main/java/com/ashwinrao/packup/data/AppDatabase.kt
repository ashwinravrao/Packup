package com.ashwinrao.packup.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ashwinrao.packup.data.model.Box
import com.ashwinrao.packup.data.model.Item
import com.ashwinrao.packup.data.repo.dao.BoxDao
import com.ashwinrao.packup.data.repo.dao.ItemDao

@Database(entities = [Box::class, Item::class], version = 4, exportSchema = false)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun boxDao() : BoxDao
    abstract fun itemDao() : ItemDao

}

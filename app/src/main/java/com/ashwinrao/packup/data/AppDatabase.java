package com.ashwinrao.packup.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ashwinrao.packup.data.model.Box;
import com.ashwinrao.packup.data.model.Item;
import com.ashwinrao.packup.data.repo.dao.BoxDao;
import com.ashwinrao.packup.data.repo.dao.ItemDao;

@Database(entities = {Box.class, Item.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BoxDao boxDao();

    public abstract ItemDao itemDao();
}

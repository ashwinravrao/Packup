package com.ashwinrao.sanbox.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ashwinrao.sanbox.data.model.Box;
import com.ashwinrao.sanbox.data.model.Item;
import com.ashwinrao.sanbox.data.repo.dao.BoxDao;
import com.ashwinrao.sanbox.data.repo.dao.ItemDao;

@Database(entities = {Box.class, Item.class}, version = 1, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BoxDao boxDao();

    public abstract ItemDao itemDao();
}

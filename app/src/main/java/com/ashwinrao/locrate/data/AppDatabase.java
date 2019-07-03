package com.ashwinrao.locrate.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Move.class, Box.class}, version = 3, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BoxDao boxDao();

    public abstract MoveDao moveDao();
}

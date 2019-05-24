package com.ashwinrao.boxray.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = Box.class, version = 3, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class BoxDatabase extends RoomDatabase {

    public abstract BoxDao dao();
}

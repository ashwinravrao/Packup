package com.ashwinrao.boxray.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Box.class}, version = 3, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class BoxDatabase extends RoomDatabase {

    public abstract BoxDao mDao();
    private static BoxDatabase sInstance;

    public static synchronized BoxDatabase getInstance(Context context) {
        if(sInstance == null) {
            sInstance = Room
                    .databaseBuilder(context.getApplicationContext(), BoxDatabase.class, "Boxes.db")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return sInstance;
    }
}

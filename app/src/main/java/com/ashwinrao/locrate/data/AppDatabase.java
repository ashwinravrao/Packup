package com.ashwinrao.locrate.data;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.ashwinrao.locrate.data.model.Box;
import com.ashwinrao.locrate.data.model.Item;
import com.ashwinrao.locrate.data.model.Move;
import com.ashwinrao.locrate.data.repo.dao.BoxDao;
import com.ashwinrao.locrate.data.repo.dao.ItemDao;
import com.ashwinrao.locrate.data.repo.dao.MoveDao;

@Database(entities = {Move.class, Box.class, Item.class}, version = 2, exportSchema = false)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {

    public abstract BoxDao boxDao();

    public abstract MoveDao moveDao();

    public abstract ItemDao itemDao();
}

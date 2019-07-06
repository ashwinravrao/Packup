package com.ashwinrao.locrate.di;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.ashwinrao.locrate.data.repo.ItemRepository;
import com.ashwinrao.locrate.data.repo.dao.BoxDao;
import com.ashwinrao.locrate.data.AppDatabase;
import com.ashwinrao.locrate.data.repo.BoxRepository;
import com.ashwinrao.locrate.data.repo.dao.ItemDao;
import com.ashwinrao.locrate.data.repo.dao.MoveDao;
import com.ashwinrao.locrate.data.repo.MoveRepository;
import com.ashwinrao.locrate.viewmodel.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    private final AppDatabase database;

    public DatabaseModule(Application application) {
        this.database =
                Room.databaseBuilder(application.getApplicationContext(),
                        AppDatabase.class,
                        "Locrate.db")
                        .fallbackToDestructiveMigration()
                        .build();
    }

    @Provides
    @Singleton
    BoxRepository provideBoxRepository() {
        return new BoxRepository(database);
    }

    @Provides
    @Singleton
    MoveRepository provideMoveRepository() {
        return new MoveRepository(database);
    }

    @Provides
    @Singleton
    ItemRepository provideItemRepository() {
        return new ItemRepository(database);
    }

    @Provides
    @Singleton
    AppDatabase provideDatabase() {
        return database;
    }

    @Provides
    BoxDao provideBoxDao() {
        return database.boxDao();
    }

    @Provides
    MoveDao provideMoveDao() {
        return database.moveDao();
    }

    @Provides
    ItemDao provideItemDao() {
        return database.itemDao();
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(MoveRepository mr, BoxRepository br, ItemRepository ir) {
        return new ViewModelFactory(mr, br, ir);
    }

}

package com.ashwinrao.locrate.di;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.ashwinrao.locrate.data.BoxDao;
import com.ashwinrao.locrate.data.BoxDatabase;
import com.ashwinrao.locrate.data.BoxRepository;
import com.ashwinrao.locrate.viewmodel.ViewModelFactory;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DatabaseModule {

    private final BoxDatabase database;

    public DatabaseModule(Application application) {
        this.database =
                Room.databaseBuilder(application.getApplicationContext(),
                        BoxDatabase.class,
                        "Boxes.db")
                        .fallbackToDestructiveMigration()
                        .build();
    }

    @Provides
    @Singleton
    BoxRepository provideRepository() {
        return new BoxRepository(database);
    }

    @Provides
    @Singleton
    BoxDatabase provideDatabase() {
        return database;
    }

    @Provides
    BoxDao provideDao() {
        return database.dao();
    }

    @Provides
    @Singleton
    ViewModelProvider.Factory provideViewModelFactory(BoxRepository repository) {
        return new ViewModelFactory(repository);
    }

}

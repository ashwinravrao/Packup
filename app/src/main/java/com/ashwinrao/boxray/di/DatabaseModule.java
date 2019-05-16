package com.ashwinrao.boxray.di;

import android.app.Application;

import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.ashwinrao.boxray.data.BoxDao;
import com.ashwinrao.boxray.data.BoxDatabase;
import com.ashwinrao.boxray.data.BoxRepository;
import com.ashwinrao.boxray.viewmodel.BoxViewModelFactory;

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
        return new BoxViewModelFactory(repository);
    }

}

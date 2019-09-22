package com.ashwinrao.packup.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.ashwinrao.packup.data.AppDatabase
import com.ashwinrao.packup.data.repo.BoxRepository
import com.ashwinrao.packup.data.repo.ItemRepository
import com.ashwinrao.packup.viewmodel.ViewModelFactory
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DatabaseModule(application: Application) {

    private val database = Room.databaseBuilder(
            application.applicationContext,
            AppDatabase::class.java,
            "Packup.db")
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideBoxRepository() = BoxRepository(database)

    @Provides
    @Singleton
    fun provideItemRepository() = ItemRepository(database)

    @Provides
    @Singleton
    fun provideDatabase() = database

    @Provides
    @Singleton
    fun provideBoxDao() = database.boxDao()

    @Provides
    @Singleton
    fun provideItemDao() = database.itemDao()

    @Provides
    @Singleton
    fun provideViewModelFactory(boxRepo: BoxRepository, itemRepo: ItemRepository) : ViewModelProvider.Factory =
            ViewModelFactory(boxRepo, itemRepo)

}

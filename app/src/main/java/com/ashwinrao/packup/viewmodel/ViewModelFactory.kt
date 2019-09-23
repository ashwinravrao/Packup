package com.ashwinrao.packup.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ashwinrao.packup.data.AppDatabase

import com.ashwinrao.packup.data.repo.BoxRepository
import com.ashwinrao.packup.data.repo.ItemRepository

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ViewModelFactory @Inject
constructor(private val database: AppDatabase,
            private val boxRepo: BoxRepository,
            private val itemRepo: ItemRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(BoxViewModel::class.java) -> BoxViewModel(boxRepo) as T
            modelClass.isAssignableFrom(CategoryViewModel::class.java) -> CategoryViewModel() as T
            modelClass.isAssignableFrom(ItemViewModel::class.java) -> ItemViewModel(itemRepo) as T
            modelClass.isAssignableFrom(InsertionViewModel::class.java) -> InsertionViewModel(database) as T
            else -> throw IllegalArgumentException("ViewModel could not be loaded")
        }
    }
}

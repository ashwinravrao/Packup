package com.ashwinrao.packup.data.repo

import androidx.lifecycle.LiveData
import com.ashwinrao.packup.data.AppDatabase
import com.ashwinrao.packup.data.model.Item
import com.ashwinrao.packup.data.repo.dao.ItemDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemRepository @Inject constructor(database: AppDatabase) {

    private val dao: ItemDao = database.itemDao()

    fun getItems(): LiveData<List<Item>> = dao.getItems()

    fun getItemsFromBox(boxUUID: String) : LiveData<List<Item>> =
            dao.getItemsFromBox(boxUUID)

    fun insert(vararg item: Item) =
            CoroutineScope(Dispatchers.IO)
                    .launch { dao.insert(*item) }

    fun update(vararg item: Item) =
            CoroutineScope(Dispatchers.IO)
                    .launch { dao.update(*item) }

    fun delete(vararg item: Item) =
            CoroutineScope(Dispatchers.IO)
                    .launch { dao.delete(*item) }
}

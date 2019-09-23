package com.ashwinrao.packup.viewmodel

import android.content.res.TypedArray
import androidx.lifecycle.ViewModel
import com.ashwinrao.packup.data.AppDatabase
import com.ashwinrao.packup.data.model.Box
import com.ashwinrao.packup.data.model.Item
import com.ashwinrao.packup.data.repo.dao.BoxDao
import com.ashwinrao.packup.data.repo.dao.ItemDao
import com.ashwinrao.packup.view.fragment.pages.ItemsPage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

/**
 *
 * This ViewModel touches the database directly, which violates Repository pattern. However, it is
 * necessary to ensure the foreign key constraint on the Item entity is fulfilled by insertion of
 * a box prior to insertion of the enclosing items.
 *
 */

@Singleton
class InsertionViewModel @Inject constructor(database: AppDatabase) : ViewModel() {

    private val boxDao: BoxDao = database.boxDao()
    private val itemDao: ItemDao = database.itemDao()

    fun saveBox(box: Box, items: List<Item>) {
        if (checkModelConstraints(box, items)) {
            CoroutineScope(Dispatchers.IO).launch {
                save(box, items)
            }
        }
    }

    private suspend fun save(box: Box, items: List<Item>) {
        boxDao.insert(box)
        itemDao.insert(*items.toTypedArray())
    }

    private fun checkModelConstraints(box: Box, items: List<Item>) = box.name!!.isNotEmpty() && !items.isNullOrEmpty()

}
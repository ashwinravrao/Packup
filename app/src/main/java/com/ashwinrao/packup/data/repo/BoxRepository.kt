package com.ashwinrao.packup.data.repo

import androidx.lifecycle.LiveData
import com.ashwinrao.packup.data.AppDatabase
import com.ashwinrao.packup.data.model.Box
import com.ashwinrao.packup.data.repo.dao.BoxDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BoxRepository @Inject constructor(database: AppDatabase) {

    private val dao: BoxDao = database.boxDao()

    fun getBoxes(): LiveData<List<Box>> = dao.getBoxes()

    fun getBoxByNumber(id: Int): LiveData<Box> = dao.getBoxByNumber(id)

    fun getBoxByUUID(uuid: String): LiveData<Box> = dao.getBoxByUUID(uuid)

    fun insert(vararg box: Box) =
            CoroutineScope(Dispatchers.IO)
                    .launch { dao.insert(*box) }

    fun update(vararg box: Box) =
            CoroutineScope(Dispatchers.IO)
                    .launch { dao.update(*box) }

    fun delete(vararg box: Box) =
            CoroutineScope(Dispatchers.IO)
                    .launch { dao.delete(*box) }
}

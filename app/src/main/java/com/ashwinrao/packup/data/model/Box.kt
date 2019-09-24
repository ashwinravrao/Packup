package com.ashwinrao.packup.data.model

import java.util.ArrayList
import java.util.Date
import java.util.UUID
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "boxes",
        indices = [Index("id")])

class Box {

    @PrimaryKey
    @ColumnInfo(name = "id")
    var id = UUID.randomUUID().toString()

    @ColumnInfo(name = "number")
    var number: Int = 0

    @ColumnInfo(name = "name")
    var name: String? = null

    @ColumnInfo(name = "description")
    var description = ""

    @ColumnInfo(name = "created")
    var createdDate = Date()

    @ColumnInfo(name = "category")
    var categories: List<String> = ArrayList()

    @ColumnInfo(name = "is_full")
    var isFull: Boolean = false

    @ColumnInfo(name = "is_tag_registered")
    var isTagRegistered: Boolean = false
}
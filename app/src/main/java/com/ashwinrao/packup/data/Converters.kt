package com.ashwinrao.packup.data

import androidx.room.TypeConverter
import java.util.*

class Converters {

    @TypeConverter
    fun timestampToDate(timestamp: Long? = null) : Date? = if (timestamp == null) null else Date(timestamp)

    @TypeConverter
    fun dateToTimestamp(date: Date? = null) = date?.time

    @TypeConverter
    fun listToString(items: List<String>) : String = items.joinToString()

    @TypeConverter
    fun stringToList(items: String) : List<String> = items.split(",").map { it.trim() }

}

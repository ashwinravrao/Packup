package com.ashwinrao.packup.data

import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.*
import com.google.gson.reflect.TypeToken



class Converters {

    @TypeConverter
    fun timestampToDate(timestamp: Long? = null) : Date? = if (timestamp == null) null else Date(timestamp)

    @TypeConverter
    fun dateToTimestamp(date: Date? = null) = date?.time

    @TypeConverter
    fun listToString(items: List<String>?) : String = Gson().toJson(items)

    @TypeConverter
    fun stringToList(items: String) : List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(items, listType)
    }

}

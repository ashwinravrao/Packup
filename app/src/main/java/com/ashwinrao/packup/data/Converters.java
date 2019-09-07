package com.ashwinrao.packup.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import androidx.room.TypeConverter;

public class Converters {

    @TypeConverter
    public static Date timestampToDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static String listToString(List<String> items) {
        return new Gson().toJson(items);
    }

    @TypeConverter
    public static List<String> stringToList(String list) {
        Type listType = new TypeToken<List<String>>() {}.getType();
        return new Gson().fromJson(list, listType);
    }

}

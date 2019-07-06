package com.ashwinrao.locrate.data;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.room.TypeConverter;
import androidx.room.util.StringUtil;

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

    @TypeConverter
    public static String latLngToString(LatLng latLng) {
        return latLng.toString();
    }

    @TypeConverter
    public static LatLng stringToLatLng(String latLng) {
        Matcher m = Pattern.compile("\\((.*?)\\)").matcher(latLng);
        String[] extr = new String[2];
        if(m.find()) extr = m.group(1).split(",");
        return new LatLng(Double.valueOf(extr[0]), Double.valueOf(extr[1]));
    }

}

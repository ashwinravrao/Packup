package com.ashwinrao.locrate.data.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "moves", indices = @Index("id"))
public class Move {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id = "";

    @ColumnInfo(name = "origin")
    private String originLatLng;

    @ColumnInfo(name = "destination")
    private String destinationLatLng;

    public Move() { }

    @NonNull
    public String getId() {
        return id;
    }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public String getOriginLatLng() {
        return originLatLng;
    }

    public void setOriginLatLng(String originLatLng) {
        this.originLatLng = originLatLng;
    }

    public String getDestinationLatLng() {
        return destinationLatLng;
    }

    public void setDestinationLatLng(String destinationLatLng) {
        this.destinationLatLng = destinationLatLng;
    }
}

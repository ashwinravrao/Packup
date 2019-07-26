package com.ashwinrao.locrate.data.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;


@Entity(tableName = "boxes", indices = @Index("id"))
public class Box {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "id")
    private String id = UUID.randomUUID().toString();

    @ColumnInfo(name = "number")
    private int number;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description = "";

    @ColumnInfo(name = "created")
    private Date createdDate = new Date();

    @ColumnInfo(name = "category")
    private List<String> categories = new ArrayList<>();

    @ColumnInfo(name = "is_full")
    private boolean isFull;

    public Box() { }

    public void setId(@NonNull String id) {
        this.id = id;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public boolean isFull() {
        return isFull;
    }

    @NonNull
    public String getId() {
        return id;
    }

    public int getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setFull(boolean full) {
        isFull = full;
    }

    public List<String> getCategories() {
        return categories;
    }

    public void setCategories(List<String> categories) {
        this.categories = categories;
    }
}
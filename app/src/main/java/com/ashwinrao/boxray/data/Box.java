package com.ashwinrao.boxray.data;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "boxes")
public class Box {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "source")
    private String source;

    @ColumnInfo(name = "destination")
    private String destination;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "created")
    private Date createdDate;

    @ColumnInfo(name = "contents")
    private List<String> contents;

    @ColumnInfo(name = "favorite")
    private boolean favorite;

    @ColumnInfo(name = "numItems")
    private String numItems;

    public Box() {
        this.createdDate = new Date();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public void setNumItems(String numItems) {
        this.numItems = numItems;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
        if(this.contents == null || this.contents.size() == 0) {
            setNumItems("No items");
        } else {
            setNumItems(String.format(Locale.US, "%d items", this.contents.size()));
        }
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSource() {
        return source;
    }

    public String getDestination() {
        return destination;
    }

    public String getDescription() {
        return description == null ? "No description" : description;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public List<String> getContents() {
        return contents;
    }

    public String getNumItems() {
        return this.numItems == null ? "Empty box" : this.numItems;
    }

    public boolean isFavorite() {
        return favorite;
    }
}
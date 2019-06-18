package com.ashwinrao.boxray.data;

import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.PrimaryKey;

@Fts4
@Entity(tableName = "boxes")
public class Box {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "rowid")
    private int id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "created")
    private Date createdDate;

    @ColumnInfo(name = "contents")
    private List<String> contents;

    @ColumnInfo(name = "numItems")
    private String numItems;

    @ColumnInfo(name = "priority")
    private boolean priority;

    public Box() {
        this.createdDate = new Date();
    }

    public void setId(int id) {
        this.id = id;
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

    public void setNumItems(String numItems) {
        this.numItems = numItems;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
        if(this.contents.size() == 0) {
            setNumItems("Nothing");
        } else if(this.contents.size() == 1) {
            setNumItems("1 item");
        } else {
            setNumItems(String.format(Locale.US, "%d items", this.contents.size()));
        }
    }

    public void setPriority(boolean priority) {
        this.priority = priority;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    public boolean isPriority() {
        return priority;
    }
}
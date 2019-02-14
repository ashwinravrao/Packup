package com.ashwinrao.boxray.data;

import java.util.Date;
import java.util.List;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "boxes")
public class Box {

    @PrimaryKey
    @ColumnInfo(name = "id")
    private int mId;

    @ColumnInfo(name = "name")
    private String mName;

    @ColumnInfo(name = "source")
    private String mSource;

    @ColumnInfo(name = "destination")
    private String mDestination;

    @ColumnInfo(name = "created")
    private Date mCreatedDate;

    @ColumnInfo(name = "contents")
    private List<String> mContents;

    @ColumnInfo(name = "favorite")
    private boolean mFavorite;

    @ColumnInfo(name = "notes")
    private String mNotes;

    public Box(int id, String name, String source, String destination, String notes) {
        mId = id;
        mName = name;
        mSource = source;
        mDestination = destination;
        mNotes = notes;
        mCreatedDate = new Date();
    }

    public List<String> getContents() {
        return mContents;
    }

    public void setContents(List<String> contents) {
        mContents = contents;
    }

    public Date getCreatedDate() {
        return mCreatedDate;
    }

    public void setCreatedDate(Date createdDate) {
        mCreatedDate = createdDate;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public String getSource() {
        return mSource;
    }

    public void setSource(String source) {
        mSource = source;
    }

    public String getDestination() {
        return mDestination;
    }

    public void setDestination(String destination) {
        mDestination = destination;
    }

    public boolean isFavorite() {
        return mFavorite;
    }

    public void setFavorite(boolean favorite) {
        mFavorite = favorite;
    }

    public String getNotes() {
        return mNotes;
    }

    public void setNotes(String notes) {
        mNotes = notes;
    }
}
package com.ashwinrao.boxray.data;

import java.util.Date;
import java.util.List;

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

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "created")
    private Date createdDate;

    @ColumnInfo(name = "contents")
    private List<String> contents;

    @ColumnInfo(name = "favorite")
    private boolean favorite;

    public Box(int id, String name, String source, String destination, String notes, Date createdDate, List<String> contents, boolean favorite) {
        this.id = id;
        this.name = name;
        this.source = source;
        this.destination = destination;
        this.notes = notes;
        this.createdDate = createdDate;
        this.contents = contents;
        this.favorite = favorite;
    }

    public List<String> getContents() {
        return contents;
    }

    public void setContents(List<String> contents) {
        this.contents = contents;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public static class Builder {

        private int id;
        private String name;
        private String source;
        private String destination;
        private String notes;
        private Date createdDate;
        private List<String> contents;
        private boolean favorite;

        public Builder() {
            this.createdDate = new Date();
        }

        public Builder setId(int id) {
            this.id = id;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setSource(String source) {
            this.source = source;
            return this;
        }

        public Builder setDestination(String destination) {
            this.destination = destination;
            return this;
        }

        public Builder setNotes(String notes) {
            this.notes = notes;
            return this;
        }

        public Builder setContents(List<String> contents) {
            this.contents = contents;
            return this;
        }

        public Builder setFavorite(boolean favorite) {
            this.favorite = favorite;
            return this;
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

        public String getNotes() {
            return notes;
        }

        public Date getCreatedDate() {
            return createdDate;
        }

        public List<String> getContents() {
            return contents;
        }

        public boolean isFavorite() {
            return favorite;
        }

        public Box build() {
            return new Box(this.id, this.name, this.source, this.destination, this.notes, this.createdDate, this.contents, this.favorite);
        }


    }


}
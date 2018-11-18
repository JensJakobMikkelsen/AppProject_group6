package com.example.memerun.classes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "URL_table")
public class memeURL {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "URL")
    private String URL;

    public memeURL()
    {

    }

    public memeURL(String url)
    {
        URL = url;
    }


    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }
}

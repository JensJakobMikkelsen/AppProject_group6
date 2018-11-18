package com.example.memerun.classes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "achievement_table")
public class achievement {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "imageName")
    public String imageName;

    @ColumnInfo(name = "requirement")
    public String requirement;

    @ColumnInfo(name = "unlocked")
    public boolean unlocked = false;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public achievement()
    {

    }

    public achievement(String name_id)
    {
        imageName = name_id;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void setUnlocked(boolean unlocked) {
        this.unlocked = unlocked;
    }
}

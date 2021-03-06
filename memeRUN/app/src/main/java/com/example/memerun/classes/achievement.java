package com.example.memerun.classes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Bitmap;

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

    @ColumnInfo(name = "steps")
    public int steps;

    @Ignore
    public Bitmap bm = null;

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public Bitmap getBm() {
        return bm;
    }

    public void setBm(Bitmap bm) {
        this.bm = bm;
    }

    public int getSteps() {
        return steps;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public achievement()
    {
    }

    public String getRequirement() {
        return requirement;
    }

    public achievement(String requirement, int steps)
    {
        this.requirement = requirement;
        this.steps = steps;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
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

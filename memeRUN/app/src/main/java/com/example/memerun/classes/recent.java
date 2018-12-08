package com.example.memerun.classes;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

@Entity(tableName = "recent_table")
public class recent {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "steps")
    private double steps;

    @ColumnInfo(name = "rating")
    private String rating;

    int lastPosition;

    public int getLastPosition() {
        return lastPosition;
    }

    public void setLastPosition(int lastPosition) {
        this.lastPosition = lastPosition;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public recent()
    {

    }

    public recent(double steps_)
    {
        Long tsLong = System.currentTimeMillis()/1000;
        date = getDateCurrentTimeZone(tsLong);
        steps = steps_;
    }

    @Ignore
    public achievement tempAchievement = new achievement();

    public achievement getTempAchievement() {
        return tempAchievement;
    }

    public void setTempAchievement(achievement tempAchievement) {
        this.tempAchievement = tempAchievement;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    public void setSteps(double steps_) {
        this.steps = steps_;
    }
    public double getSteps() {
        return this.steps;
    }




    //https://stackoverflow.com/questions/18717111/to-get-date-and-time-from-timestamp-android

    public String getDateCurrentTimeZone(long timestamp) {
        try{
            Calendar calendar = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone("Denmark");
            calendar.setTimeInMillis(timestamp * 1000);
            calendar.add(Calendar.MILLISECOND, tz.getOffset(calendar.getTimeInMillis()));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date currentTimeZone = (Date) calendar.getTime();
            return sdf.format(currentTimeZone);
        }catch (Exception e) {
        }
        return "";
    }

}

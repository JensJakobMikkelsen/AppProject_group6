package com.example.memerun.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.memerun.classes.achievement;
import com.example.memerun.classes.memeURL;

import java.util.List;

@Dao
public interface daoAccess {

    // memeURL

    @Insert
    void insert(memeURL url);

    @Query("DELETE FROM URL_table")
    void deleteAll_URLs();

    @Update
    void update(memeURL url);

    @Delete
    void delete(memeURL url);


    //Sorterer efter URL
    @Query("SELECT * from URL_table ORDER BY URL ASC")
    List<memeURL> getAllURLS();

    //Sorterer efter ID
    @Query("SELECT * from URL_table ORDER BY ID ASC")
    List<memeURL> getAllURLSbyID();

    @Update
    void updateAll(memeURL... memeURLS);


    // achievements

    @Insert
    void insert(achievement achievement_);

    @Query("DELETE FROM achievement_table")
    void deleteAll_achievements();

    @Update
    void update(achievement achievement_);

    @Delete
    void delete(achievement achievement_);

    @Query("SELECT * from achievement_table ORDER BY imageName ASC")
    List<achievement> getAll_Achievements();

    @Query("SELECT * from achievement_table ORDER BY ID ASC")
    List<achievement> getAll_AchievementsByID();

    @Update
    void updateAll(achievement... achievements);






}

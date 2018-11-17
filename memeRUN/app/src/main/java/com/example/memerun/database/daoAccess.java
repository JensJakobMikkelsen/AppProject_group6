package com.example.memerun.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.memerun.classes.memeURL;

import java.util.List;

@Dao
public interface daoAccess {

    @Insert
    void insert(memeURL url);

    @Query("DELETE FROM URL_table")
    void deleteAll();

    @Update
    void update(memeURL url);

    @Delete
    void delete(memeURL url);

    @Query("SELECT * from URL_table ORDER BY URL ASC")
    List<memeURL> getAllURLS();

    @Update
    void updateAll(memeURL... memeURLS);

}

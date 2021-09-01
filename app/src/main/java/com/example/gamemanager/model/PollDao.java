package com.example.gamemanager.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PollDao {
    @Query("select * from Poll")
    LiveData<List<Poll>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Poll... polls);

    @Delete
    void delete(Poll poll);
}

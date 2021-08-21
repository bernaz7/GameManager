package com.example.gamemanager.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GameDao {
    @Query("select * from Game")
    LiveData<List<Game>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Game... games);

    @Delete
    void delete(Game game);
}

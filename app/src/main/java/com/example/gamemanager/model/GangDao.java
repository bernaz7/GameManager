package com.example.gamemanager.model;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GangDao {
    @Query("select * from Gang")
    LiveData<List<Gang>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Gang... gangs);

    @Delete
    void delete(Gang student);
}

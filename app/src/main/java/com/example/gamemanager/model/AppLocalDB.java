package com.example.gamemanager.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.gamemanager.GameManagerApp;

@Database(entities = {Gang.class, UserData.class}, version = 3)
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract GangDao gangDao();
    public abstract UserDataDao userDataDao();
}
public class AppLocalDB {
    final static public AppLocalDbRepository db =
            Room.databaseBuilder(GameManagerApp.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}

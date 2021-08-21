package com.example.gamemanager.model;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.example.gamemanager.GameManagerApp;

@Database(entities = {Gang.class, UserData.class, Poll.class, Game.class}, version = 3)
@TypeConverters({Converters.class})
abstract class AppLocalDbRepository extends RoomDatabase {
    public abstract GangDao gangDao();
    public abstract UserDataDao userDataDao();
    public abstract PollDao pollDao();
    public abstract GameDao gameDao();
}
public class AppLocalDB {
    final static public AppLocalDbRepository db =
            Room.databaseBuilder(GameManagerApp.context,
                    AppLocalDbRepository.class,
                    "dbFileName.db")
                    .fallbackToDestructiveMigration()
                    .build();
}

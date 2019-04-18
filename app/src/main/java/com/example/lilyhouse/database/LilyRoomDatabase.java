package com.example.lilyhouse.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.lilyhouse.daos.MangaCoverItemDao;
import com.example.lilyhouse.models.MangaCoverItem;

@Database(entities = {MangaCoverItem.class}, version = 1, exportSchema = false)
public abstract class LilyRoomDatabase extends RoomDatabase {
    private static volatile LilyRoomDatabase mInstance;

    public static LilyRoomDatabase getInstance(Context context) {
        if (mInstance == null) {
            synchronized (LilyRoomDatabase.class) {
                mInstance = Room.databaseBuilder(context.getApplicationContext(),
                        LilyRoomDatabase.class, "lily_database").build();
            }
        }
        return mInstance;
    }

    public abstract MangaCoverItemDao coverItemDao();
}

package com.example.lilyhouse.daos;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.example.lilyhouse.models.MangaCoverItem;

import java.util.List;

@Dao
public interface MangaCoverItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertItems(MangaCoverItem... items);

    @Query("DELETE FROM manga_cover_item_table")
    void deleteAllItems();

    @Query("SELECT * FROM manga_cover_item_table ORDER BY jointimenanos ASC")
    LiveData<List<MangaCoverItem>> getAllItems();
}

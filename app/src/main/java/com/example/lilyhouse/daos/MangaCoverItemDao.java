package com.example.lilyhouse.daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

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

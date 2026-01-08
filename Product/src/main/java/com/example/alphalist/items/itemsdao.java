package com.example.alphalist.items;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.alphalist.model.Items;

import java.util.List;

@Dao
public interface itemsdao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Items it);

    @Update
    void update(Items it);

    @Delete
    void delete(Items it);

    @Query("SELECT * FROM item WHERE catid = :groupId") //write in the model
    LiveData<List<Items>> getItemList(long groupId);

    @Query("SELECT * FROM item")
    LiveData<List<Items>> getItems();
}


package com.example.alphalist.io.cat;
import com.example.alphalist.model.ItemsList;
import com.example.alphalist.model.Cat;
import androidx.lifecycle.LiveData;
import java.util.List;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
@Dao
public interface catdao {
    @Insert
    void insert(Cat currcat);

    @Update
    void update(Cat currcat);

    @Delete
    void delete(Cat currcat);

    @Query("SELECT * FROM cat ORDER BY name")
    LiveData<List<Cat>> getAllcats();

    @Query("SELECT * FROM cat ORDER BY name")
    LiveData<List<ItemsList>> getAllitemslists();

}

package com.example.alphalist.transaction;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.alphalist.model.Transaction;

import java.util.List;

@Dao
public interface transdao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Transaction transaction);

    @Update
    void update(Transaction transaction);
    @Delete
    void delete(Transaction transaction);

    @Query("SELECT * FROM purchase_transaction")
    LiveData<List<Transaction>> getTransactions();

    @Query("SELECT * FROM purchase_transaction WHERE catid = :catid")
    LiveData<List<Transaction>> gettranscat(int catid);

    @Query("SELECT * FROM purchase_transaction WHERE itemid = :itemid")
    LiveData<List<Transaction>> gettransitems(int itemid);
}


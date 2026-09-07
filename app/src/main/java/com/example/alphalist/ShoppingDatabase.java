package com.example.alphalist;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.alphalist.io.cat.catdao;
import com.example.alphalist.items.itemsdao;
import com.example.alphalist.transaction.transdao;


import com.example.alphalist.model.Cat;
import com.example.alphalist.model.Items;
import com.example.alphalist.model.Transaction;
@Database(entities = {Cat.class, Items.class, Transaction.class}, version = 9)
public abstract class ShoppingDatabase extends RoomDatabase {
    private static volatile ShoppingDatabase instance;
    public abstract catdao shoppingGroupDao();

    public abstract itemsdao merchandiseDao();

    public abstract transdao transactionDao();

    public static synchronized ShoppingDatabase getInstance(Context context) {
        if (instance == null) {// only creates instance when there does not exist one already
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            ShoppingDatabase.class, "shopping_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

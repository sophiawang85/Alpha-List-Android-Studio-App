package com.example.alphalist.ui.newitem;

import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.ViewModel;

import com.example.alphalist.model.Transaction;
import com.example.alphalist.model.Cat;
import com.example.alphalist.model.Items;
import java.time.LocalDate;

public class NewItemViewModel extends ViewModel {
    public Items createMerchandise(String name, String type, Double targetPrice, int shoppingGroupId) {
        //add validation
        return new Items(name, targetPrice, type, shoppingGroupId);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public Transaction createTransaction(Cat shoppingGroup, Items merchandise, Double purchasePrice, Integer purchasedUnits) {
        final Transaction transaction = new Transaction();
        transaction.setItemid(merchandise.getId());
        transaction.setItemname(merchandise.getName());
        transaction.setItemType(merchandise.getType());
        transaction.setCatid(shoppingGroup.getId());
        transaction.setCatname(shoppingGroup.getName());
        transaction.setTargetPrice(merchandise.getPrice());
        transaction.setPurchasedUnit(purchasedUnits);

        transaction.setActualPrice(purchasePrice);
        transaction.setDate(LocalDate.now().toString());
        return transaction;
    }
}
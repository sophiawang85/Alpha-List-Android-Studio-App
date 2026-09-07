package com.example.alphalist.ui.it;

import android.content.Context;
import android.os.Build;

import androidx.annotation.RequiresApi;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.example.alphalist.model.Transaction;
import com.example.alphalist.model.Items;
import com.example.alphalist.items.itemsrepo;
import com.example.alphalist.transaction.transrepo;
import java.util.List;

import lombok.NonNull;

public class ItemListViewModel extends ViewModel {
    private final itemsrepo itRepository;
    private final transrepo transactionRepository;

    private final LiveData<List<Items>> it;

    public ItemListViewModel(@NonNull Context context) {
        this.itRepository = new itemsrepo(context);
        this.transactionRepository = new transrepo(context);
        this.it = this.itRepository.getItems();
    }

    public LiveData<List<Items>> getItems() {
        return it;
    }


    //api too low???
    @RequiresApi(api = Build.VERSION_CODES.N)
    public String[] getExistingItemsTypes() {
        if (it.getValue() == null) return new String[0];
        return it.getValue()
                .stream()
                .map(Items::getType)
                .distinct()
                .toArray(String[]::new);//put into an array
    }

    public void insert(Items merchandise) {
        itRepository.insert(merchandise);
    }

    public void update(Items merchandise) {
        if (merchandise != null) {
            itRepository.update(merchandise);
        }
    }

    public void delete(Items merchandise) {
        if (merchandise != null) {
            itRepository.delete(merchandise);
        }
    }

    public void insert(Transaction transaction) {transactionRepository.insert(transaction);}
}
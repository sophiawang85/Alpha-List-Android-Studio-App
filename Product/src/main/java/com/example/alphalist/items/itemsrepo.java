package com.example.alphalist.items;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.alphalist.ShoppingDatabase;
import com.example.alphalist.model.Items;
import com.example.alphalist.items.itemsdao;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class itemsrepo {
    private final itemsdao currdao;

    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public itemsrepo(Context context) {
        ShoppingDatabase database = ShoppingDatabase.getInstance(context);
        currdao = database.merchandiseDao();
    }
    public LiveData<List<Items>> getItems(int catid) {
        return currdao.getItemList(catid);
    }

    public LiveData<List<Items>> getItems() {
        return currdao.getItems();
    }

    public void insert(Items it) {
        executorService.execute(() -> currdao.insert(it));
    }

    public void update(Items it) {
        executorService.execute(() -> currdao.update(it));
    }

    public void delete(Items it) {
        executorService.execute(() -> currdao.delete(it));
    }
}

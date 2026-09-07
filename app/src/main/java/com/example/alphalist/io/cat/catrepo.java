package com.example.alphalist.io.cat;
import android.content.Context;

import androidx.lifecycle.LiveData;
import com.example.alphalist.ShoppingDatabase;
import com.example.alphalist.model.ItemsList;
import com.example.alphalist.model.Cat;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class catrepo {
    private final catdao cdao;
    private final LiveData<List<Cat>> currcat;
    private final LiveData<List<ItemsList>> currlist;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public catrepo(Context c) {
        ShoppingDatabase database = ShoppingDatabase.getInstance(c);
        cdao = database.shoppingGroupDao();
        currcat = cdao.getAllcats();
        currlist = cdao.getAllitemslists();
    }

    public LiveData<List<Cat>> getCurrcat() {
        return currcat;
    }
    public LiveData<List<ItemsList>> getItemsList() {
        return currlist;
    }

    public void insert(Cat newcat) {
        executorService.execute(() -> cdao.insert(newcat));
    }

    public void update(Cat newcat) {
        executorService.execute(() -> cdao.update(newcat));
    }

    public void delete(Cat newcat) {
        executorService.execute(() -> cdao.delete(newcat));
    }
}

/* Uses executorService to call database operation(insert/update/delete/read) method in separate thread, an asynchronized way.
 Otherwise, will cause error */

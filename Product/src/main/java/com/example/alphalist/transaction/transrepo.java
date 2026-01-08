package com.example.alphalist.transaction;
import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.alphalist.ShoppingDatabase;
import com.example.alphalist.model.Transaction;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class transrepo {
    private final transdao tdao;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    public transrepo(Context context) {
        ShoppingDatabase database = ShoppingDatabase.getInstance(context);
        tdao = database.transactionDao();
    }

    public LiveData<List<Transaction>> getTransactions() {
        return tdao.getTransactions();
    }

    public LiveData<List<Transaction>> gettransforcat(int catid) {
        return tdao.gettranscat(catid);
    }

    public  LiveData<List<Transaction>> getTransactionsForMerchandise(int merchandiseId) {
        return tdao.gettransitems(merchandiseId);
    }

    public void insert(Transaction transaction) {
        executorService.execute(() -> tdao.insert(transaction));
    }

    public void update(Transaction transaction) {
        executorService.execute(() -> tdao.update(transaction));
    }

    public void delete(Transaction transaction) {
        executorService.execute(() -> tdao.delete(transaction));
    }
}

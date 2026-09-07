package com.example.alphalist.ui.dashboard;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.alphalist.model.Transaction;
import com.example.alphalist.transaction.transrepo;

import java.util.List;

import lombok.NonNull;

public class DashboardViewModel extends ViewModel {

    private final transrepo transactionRepository;
    private final LiveData<List<Transaction>> transactions;

    public DashboardViewModel(@NonNull Context context) {
        this.transactionRepository = new transrepo(context);
        this.transactions = transactionRepository.getTransactions();
    }

    public LiveData<List<Transaction>> getTransactions() {
        return transactions;
    }
}
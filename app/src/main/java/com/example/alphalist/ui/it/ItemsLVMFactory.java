package com.example.alphalist.ui.it;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class ItemsLVMFactory implements ViewModelProvider.Factory {
    private final Context context;
    public ItemsLVMFactory(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.isAssignableFrom(ItemListViewModel.class)) {
            return (T) new ItemListViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}


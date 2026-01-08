package com.example.alphalist.ui.home;

import androidx.lifecycle.ViewModelProvider;
import android.content.Context;

import lombok.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

public class HVMFactory implements ViewModelProvider.Factory {
    private Context context;

    public HVMFactory(Context context){this.context = context;}


    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass){
        if (modelClass.isAssignableFrom(HomeViewModel.class)){
            return (T) new HomeViewModel(context);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

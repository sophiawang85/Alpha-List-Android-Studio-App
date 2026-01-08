package com.example.alphalist.ui.home;


import android.widget.Button;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.alphalist.io.cat.catrepo;
import com.example.alphalist.model.ItemsList;
import com.example.alphalist.model.Cat;
import android.content.Context;

import java.util.List;

import lombok.NonNull;

public class HomeViewModel extends ViewModel {
    private catrepo shoppingGroupRepository;
    private LiveData<List<ItemsList>> shoppingGroupWithMerchandiseList;

    public HomeViewModel(@NonNull Context context) {
        this.shoppingGroupRepository = new catrepo(context);
        this.shoppingGroupWithMerchandiseList = shoppingGroupRepository.getItemsList();
    }

    public LiveData<List<ItemsList>> getCat() {
        return this.shoppingGroupWithMerchandiseList;
    }

    public void insert(Cat shoppingGroup) {
        shoppingGroupRepository.insert(shoppingGroup);
    }

    public void update(Cat shoppingGroup) {
        if (shoppingGroup != null) {
            shoppingGroupRepository.update(shoppingGroup);
        }
    }

    public void delete(Cat shoppingGroup) {
        if (shoppingGroup != null) {
            shoppingGroupRepository.delete(shoppingGroup);
        }
    }
}
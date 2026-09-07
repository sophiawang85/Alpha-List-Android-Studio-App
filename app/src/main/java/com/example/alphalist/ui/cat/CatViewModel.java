package com.example.alphalist.ui.cat;

import androidx.lifecycle.ViewModel;

import com.example.alphalist.model.Cat;

public class CatViewModel extends ViewModel {
    public CatViewModel() {
    }

    public Cat createCat(String name, String description) {
        //add validation
        return new Cat(name, description);
    }
}
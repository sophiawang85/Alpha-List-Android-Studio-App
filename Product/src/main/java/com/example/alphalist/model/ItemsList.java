package com.example.alphalist.model;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class ItemsList implements Parcelable {
    @Embedded
    public Cat cat;

    @Relation(
            parentColumn = "id",
            entityColumn = "catid"
    )
    public List<Items> itemslist;
    public ItemsList(){

    }
    protected ItemsList(Parcel in) {
        cat = in.readParcelable(Cat.class.getClassLoader());
        itemslist = in.createTypedArrayList(Items.CREATOR);
    }
    public static final Creator<ItemsList> CREATOR = new Creator<ItemsList>() {
        @Override
        public ItemsList createFromParcel(Parcel in) {
            return new ItemsList(in);
        }

        @Override
        public ItemsList[] newArray(int size) {
            return new ItemsList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeParcelable(cat, flags);
        dest.writeTypedList(itemslist);
    }
}

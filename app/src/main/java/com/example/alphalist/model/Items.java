package com.example.alphalist.model;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

@Entity(tableName = "item",
        foreignKeys = @ForeignKey(entity = Cat.class,
                parentColumns = "id",
                childColumns = "catid",
                onDelete = ForeignKey.CASCADE))
//@TypeConverters(itemsTypeConvert.class)

public class Items implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String description;
    private Double price;
    private String type;

    private int catid;
    public Items(String name, Double price, String type, int catid)
    {
        this.name = name;
        //this.description = description;
        this.price = price;
        this.type = type;
        this.catid = catid;
    }
    public Items(Parcel in)
    {
        id = in.readInt();
        description = in.readString();
        price = in.readDouble();
        type = in.readString();
        catid = in.readInt();
    }
    public static final Creator<Items> CREATOR = new Creator<Items>() {
        @Override
        public Items createFromParcel(Parcel in) {
            return new Items(in);
        }

        @Override
        public Items[] newArray(int size) {
            return new Items[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCatid() {
        return catid;
    }

    public void setCatid(int catid) {
        this.catid = catid;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(description);
        dest.writeDouble(price);
        dest.writeString(type);
        dest.writeInt(catid);
    }
}

package com.example.alphalist.model;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "purchase_transaction")
public class Transaction {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long id;
    private String date;
    private Double targetPrice;

    private Double actualPrice;
    private int itemid;
    private String itemname;
    private Integer purchasedUnit;
    private String itemType;
    private int catid;
    private String catname;

    public long getId() {return id;}
    public void setId (long id) {this.id = id;}
    public String getDate() {return date;}
    public void setDate(String date){this.date = date;}
    public Double getTargetPrice() {return targetPrice;}
    public void setTargetPrice(Double targetPrice) {this.targetPrice = targetPrice;}
    public Double getActualPrice(){return actualPrice;}
    public void setActualPrice(Double actualPrice){this.actualPrice = actualPrice;}
    public int getItemid(){return itemid;}
    public void setItemid(int merchandiseId) {this.itemid = merchandiseId;}
    public String getItemname(){return itemname;}
    public Integer getPurchasedUnit() {
        return purchasedUnit;
    }

    public void setPurchasedUnit(Integer purchasedUnit) {
        this.purchasedUnit = purchasedUnit;
    }

    public void setItemname(String merchandiseName){this.itemname = merchandiseName;}
    public String getItemType(){return itemType;}
    public void setItemType(String merchandiseType){this.itemType =merchandiseType;}
    public int getCatid(){return catid;}
    public void setCatid(int shoppingGroupId){this.catid = shoppingGroupId;}
    public String getCatname(){return catname;}
    public void setCatname(String shoppingGroupName){this.catname = shoppingGroupName;}
}

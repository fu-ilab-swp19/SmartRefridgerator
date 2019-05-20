package com.fub.smart.models;

public class Item {


    private String itemId;
    private String itemName;
    private String itemBrand;
    public Item() {

    }

    public Item(String itemId, String itemName, String itemBrand) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemBrand = itemBrand;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemBrand() {
        return itemBrand;
    }

    public void setItemBrand(String itemBrand) {
        this.itemBrand = itemBrand;
    }
}

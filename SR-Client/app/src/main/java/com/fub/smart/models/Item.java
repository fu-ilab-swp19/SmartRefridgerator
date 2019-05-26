package com.fub.smart.models;

public class Item {


    private String itemId;
    private String itemName;
    private String itemBrand;
    private String itemAmount;
    private String itemShelf;
    private String itemDesc;
    private String itemExpireDate;
    public Item() {

    }

    public Item(String itemId, String itemName, String itemBrand, String itemAmount, String itemShelf, String itemDesc, String itemExpireDate) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemBrand = itemBrand;
        this.itemAmount = itemAmount;
        this.itemShelf = itemShelf;
        this.itemDesc = itemDesc;
        this.itemExpireDate = itemExpireDate;
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

    public String getItemAmount() {
        return itemAmount;
    }

    public void setItemAmount(String itemAmount) {
        this.itemAmount = itemAmount;
    }

    public String getItemShelf() {
        return itemShelf;
    }

    public void setItemShelf(String itemShelf) {
        this.itemShelf = itemShelf;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }

    public String getItemExpireDate() {
        return itemExpireDate;
    }

    public void setItemExpireDate(String itemExpireDate) {
        this.itemExpireDate = itemExpireDate;
    }
}

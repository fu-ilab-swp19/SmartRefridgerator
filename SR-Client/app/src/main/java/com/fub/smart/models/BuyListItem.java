package com.fub.smart.models;

public class BuyListItem {

    private String itemId;
    private String itemName;
    private String itemReminderDate;
   // private String itemAmount;


    public BuyListItem(){

    }

    public BuyListItem(String itemId, String itemName, String itemReminderDate) {
        this.itemId = itemId;
        this.itemName = itemName;
        this.itemReminderDate = itemReminderDate;

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

    public String getItemReminderDate() {
        return itemReminderDate;
    }

    public void setItemReminderDate(String itemReminderDate) {
        this.itemReminderDate = itemReminderDate;
    }


}

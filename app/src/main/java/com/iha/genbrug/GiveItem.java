package com.iha.genbrug;


import android.graphics.drawable.Drawable;

public class GiveItem {

    private String headline;
    private String description;
    private String imageURL;
    private long itemId;
    private int amount;


    public GiveItem(String newItemName, String newDescription, String imageURL, long itemId,int amount) {
        this.headline = newItemName;
        this.description = newDescription;
        this.imageURL = imageURL;
        this.itemId = itemId;
        this.amount = amount;

    }

    public String getHeadline() {
        return headline;
    }


    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imageURL;
    }

     public long getItemId() {
        return itemId;
    }

    public int getAmount() {
        return amount;
    }

}

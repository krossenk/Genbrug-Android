package com.iha.genbrug;


import android.graphics.drawable.Drawable;

public class GiveItem {

    private String headline;
    private String description;
    private String imageURL;
    private long itemId;



    public GiveItem(String newItemName, String newDescription, String imageURL, long itemId) {
        this.headline = newItemName;
        this.description = newDescription;
        this.imageURL = imageURL;
        this.itemId = itemId;
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


}

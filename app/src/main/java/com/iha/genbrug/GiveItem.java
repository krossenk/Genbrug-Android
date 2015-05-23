package com.iha.genbrug;


import android.graphics.drawable.Drawable;

public class GiveItem {

    private String headline;
    private String description;
    private Drawable photoDrawable;
    private int itemId;


    public GiveItem(String newItemName, String newDescription, Drawable photoDrawable, int itemId) {
        this.headline = newItemName;
        this.description = newDescription;
        this.photoDrawable = photoDrawable;
        this.itemId = itemId;
    }

    public String getHeadline() {
        return headline;
    }


    public String getDescription() {
        return description;
    }

    public Drawable getPhotoDrawable() {
        return photoDrawable;
    }

     public int getItemId() {
        return itemId;
    }

}

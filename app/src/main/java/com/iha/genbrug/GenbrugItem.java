package com.iha.genbrug;

import android.graphics.drawable.Drawable;

/**
 * Created by Gladiator HelmetFace on 5/10/2015.
 */
public class GenbrugItem {
    private String headline;
    private String description;
    private String imgURL;
    private long itemId;

    public GenbrugItem(String newItemName, String newDescription, String imageURL, long itemId) {
        this.headline = newItemName;
        this.description = newDescription;
        this.imgURL = imageURL;
        this.itemId = itemId;


    }

    public String getHeadline() {
        return headline;
    }

    public void setHeadline(final String newItemName) {
        this.headline = newItemName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(final String newDescreption) {
        this.headline = newDescreption;
    }

    public String getImageURL() {
        return imgURL;
    }

    public void setImageURL(String imageUrl) {
        this.imgURL = imageUrl;
    }

    public long getItemId() {
        return itemId;
    }
}



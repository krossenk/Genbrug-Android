package com.iha.genbrug;

import android.graphics.drawable.Drawable;

/**
 * Created by Gladiator HelmetFace on 5/10/2015.
 */
public class GenbrugItem {
    private String headline;
    private String description;
    private Drawable photoDrawable;
    private long itemId;

    public GenbrugItem(String newItemName, String newDescription, Drawable newPhotoDrawable, long itemId) {
        this.headline = newItemName;
        this.description = newDescription;
        this.photoDrawable = newPhotoDrawable;
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

    public Drawable getPhotoDrawable() {
        return photoDrawable;
    }

    public void setPhotoDrawable(Drawable newPhotoDrawable) {
        this.photoDrawable = newPhotoDrawable;
    }

    public long getItemId() {
        return itemId;
    }
}



package com.iha.genbrug;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class TakeItem  {

    private String headline;
    private String description;
    private String imgURL;
    private long itemId;


    public TakeItem(String newItemName, String newDescription, String imageURL, long itemId) {
        this.headline = newItemName;
        this.description = newDescription;
        this.imgURL = imageURL;
        this.itemId = itemId;
    }

    public String getHeadline() {
        return headline;
    }


    public String getDescription() {
        return description;
    }

    public String getImageURL() {
        return imgURL;
    }

     public long getItemId() {
        return itemId;
    }



}

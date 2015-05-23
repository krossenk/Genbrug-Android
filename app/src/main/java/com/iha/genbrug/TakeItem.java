package com.iha.genbrug;

import android.graphics.drawable.Drawable;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class TakeItem  {

    private String headline;
    private String description;
    private String imagePath;
    private int itemId;


    public TakeItem(String newItemName, String newDescription, String imagePath, int itemId) {
        this.headline = newItemName;
        this.description = newDescription;
        this.imagePath = imagePath;
        this.itemId = itemId;
    }

    public String getHeadline() {
        return headline;
    }


    public String getDescription() {
        return description;
    }

    public String getImagePath() {
        return imagePath;
    }

     public int getItemId() {
        return itemId;
    }



}

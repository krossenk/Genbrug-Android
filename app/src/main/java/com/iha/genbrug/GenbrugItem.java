package com.iha.genbrug;

import android.graphics.drawable.Drawable;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import webservice.Address;
import webservice.Publication;

/**
 * Created by Gladiator HelmetFace on 5/10/2015.
 */
public class GenbrugItem {
    private String headline;
    private String description;
    private String imgURL;
    private long itemId;
    private Address address;
    private String pickupStartTime;
    private String pickupEndTime;

    public GenbrugItem(String newItemName, String newDescription, String imageURL, long itemId, Address address) {
        this.headline = newItemName;
        this.description = newDescription;
        this.imgURL = imageURL;
        this.itemId = itemId;
        this.address = address;

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

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address addr)
    {
        this.address = addr;
    }

    public String getPickupStartTime()
    {
        return pickupStartTime;
    }

    public void setPickupStartTime(String startTime)
    {
        this.pickupStartTime = startTime;
    }

    public String getPickupEndTime()
    {
        return pickupStartTime;
    }

    public void setPickupEndTime(String endTime)
    {
        this.pickupEndTime = endTime;
    }

}



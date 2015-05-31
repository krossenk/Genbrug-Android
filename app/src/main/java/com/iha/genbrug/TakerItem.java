package com.iha.genbrug;


public class TakerItem {

    private String date;
    private String pickUpTime;
    private String imgURL;
    private long itemId;


    public TakerItem(String date, String pickUpTime, String imageURL, long itemId) {
        this.date = date;
        this.pickUpTime = pickUpTime;
        this.imgURL = imageURL;
        this.itemId = itemId;
    }

    public String getDate() {
        return date;
    }

    public String getPickUpTime() {
        return pickUpTime;
    }

    public String getImageURL() {
        return imgURL;
    }

     public long getItemId() {
        return itemId;
    }



}

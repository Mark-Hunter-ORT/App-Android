package com.example.markhunters.model;

public class Marca {
    double lat, lon;
    String userId, imageId, text;
    Marca(double lat,double lon,String userId,String imageId,String text){
        setImageId(imageId);
        setLat(lat);
        setLon(lon);
        setUserId(userId);
        setText(text);

    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public String getImageId() {
        return imageId;
    }

    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }

    private void setImageId(String imageId) {
        this.imageId = imageId;
    }

    private void setLat(double lat) {
        this.lat = lat;
    }

    private void setLon(double lon) {
        this.lon = lon;
    }

    private void setUserId(String userId) {
        this.userId = userId;
    }

    private void setText(String text) {
        this.text = text;
    }
}

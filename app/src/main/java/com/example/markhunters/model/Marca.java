package com.example.markhunters.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONObject;

import java.util.Map;

public class Marca extends Model {
    com.example.markhunters.model.Location location;
    String userId, imageId, text;
    Marca (double lat,double lon, String userId, String imageId, String text){
        setImageId(imageId);
        location = new com.example.markhunters.model.Location (lat, lon);
        setUserId(userId);
        setText(text);
    }
    public Marca (Location location, String userUid) {
        this.location = new com.example.markhunters.model.Location(location.getLatitude(), location.getLongitude());
        this.userId = userUid;
    }

    public Marca (com.example.markhunters.model.Location location, String userUid) {
        this.location = location;
        this.userId = userUid;
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject();
    }

    public double getLat() {
        return location.getLat();
    }

    public LatLng getLatLng() {
        return new LatLng (getLat(), getLon());
    }

    public double getLon() {
        return location.getLon();
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

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    private void setUserId(String userId) {
        this.userId = userId;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public Map<String, Object> toDto() {
        return null;
    }

    @Override
    public String getKey() {
        return null;
    }

    public com.example.markhunters.model.Location getLocation() {
        return location;
    }
}

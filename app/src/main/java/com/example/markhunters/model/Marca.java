package com.example.markhunters.model;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Map;

public class Marca extends Model {
    double lat, lon;
    String userId, imageId, text;
    Marca (double lat,double lon,String userId,String imageId,String text){
        setImageId(imageId);
        setLat(lat);
        setLon(lon);
        setUserId(userId);
        setText(text);
    }
    public Marca (Location location) {
        setLat(location.getLatitude());
        setLon(location.getLongitude());
    }

    public double getLat() {
        return lat;
    }

    public LatLng getLatLng() {
        return new LatLng (lat, lon);
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

    public void setImageId(String imageId) {
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

    @Override
    public Map<String, Object> toDto() {
        return null;
    }

    @Override
    public String getKey() {
        return null;
    }
}

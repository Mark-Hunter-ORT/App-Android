package com.example.markhunters.model;

import java.util.Map;

public class Location extends Model{
    private double GPS_x;
    private double GPS_y;

    public Location (double lat, double lon) {
        GPS_x = lat;
        GPS_y = lon;
    }


    @Override
    public Map<String, Object> toDto() {
        return null;
    }

    @Override
    public String getKey() {
        return null;
    }

    public double getLat() {
        return GPS_x;
    }

    public double getLon() {
        return GPS_y;
    }
}

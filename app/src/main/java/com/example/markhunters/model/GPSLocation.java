package com.example.markhunters.model;

import android.location.Location;

public class GPSLocation {
    public Double GPS_X;
    public Double GPS_Y;

    public GPSLocation(Double GPS_X, Double GPS_Y){
        this.GPS_X = GPS_X;
        this.GPS_Y = GPS_Y;
    }

    public GPSLocation(Location loc){
        this.GPS_X = loc.getLatitude();
        this.GPS_Y = loc.getLongitude();
    }
}

package com.example.markhunters.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MarkLocation extends Model {
    public GPSLocation gps;
    public MagneticLocation magnetic;
    private Boolean hasMagnetic;
    public Integer id;

    public MarkLocation(GPSLocation gps){
        this.gps = gps;
        this.hasMagnetic = false;
    }

    public MarkLocation(GPSLocation gps, MagneticLocation magnetic){
        this.gps = gps;
        this.magnetic = magnetic;
        this.hasMagnetic = true;
    }

    public MarkLocation(Integer id, GPSLocation gps){
        this.id = id;
        this.gps = gps;
        this.hasMagnetic = false;
    }

    public MarkLocation(Integer id, GPSLocation gps, MagneticLocation magnetic){
        this.id = id;
        this.gps = gps;
        this.magnetic = magnetic;
        this.hasMagnetic = true;
    }

    public Boolean getHasMagnetic() {
        return this.hasMagnetic;
    }

    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        try{
            JSONObject gps = new JSONObject();
            gps.put("GPS_x", this.gps.GPS_X);
            gps.put("GPS_y", this.gps.GPS_Y);
            json.put("GPS", gps);
            if(this.hasMagnetic) {
                JSONObject magnetic = new JSONObject();
                magnetic.put("magnetic_x", this.magnetic.magnetic_x);
                magnetic.put("magnetic_y", this.magnetic.magnetic_y);
                magnetic.put("magnetic_z", this.magnetic.magnetic_z);
                json.put("magnetic", magnetic);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

    public static MarkLocation fromJson(JSONObject json){
        MarkLocation loc = null;
        try {
            if (json.has("magnetic")) {
                loc = new MarkLocation(json.getInt("id"),
                        new GPSLocation(json.getJSONObject("GPS").getDouble("GPS_x"),
                                json.getJSONObject("GPS").getDouble("GPS_y")),
                        new MagneticLocation(json.getJSONObject("magnetic").getDouble("magnetic_x"),
                                json.getJSONObject("magnetic").getDouble("magnetic_y"),
                                json.getJSONObject("magnetic").getDouble("magnetic_z")));
            }else{
                loc = new MarkLocation(json.getInt("id"),
                        new GPSLocation(json.getJSONObject("GPS").getDouble("GPS_x"),
                                json.getJSONObject("GPS").getDouble("GPS_y")));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return loc;
    }

    public static List<MarkLocation> fromJsonArray(JSONArray jsonArray) {
        final List<MarkLocation> locations = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i ++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                locations.add(fromJson(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return locations;
    }

    public LatLng getLatLng() {
        return new LatLng(gps.GPS_X, gps.GPS_Y);
    }
}

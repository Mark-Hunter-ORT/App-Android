package com.example.markhunters.model;

import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class LocationModel extends Model {
    private String uid;
    private String user_uid;
    private UserModel user;
    private String gps_uid;
    private GPSLocationModel gps;

    public LocationModel(@NotNull final String uid, @NotNull final UserModel user, @NotNull final GPSLocationModel gps) {
        this.uid = uid;
        this.user_uid = user.getUid();
        this.user = user;
        this.gps_uid = gps.getUid();
        this.gps = gps;
    }

    @NotNull
    public String getUserUid() {
        return user_uid;
    }

    @NotNull
    public UserModel getUser() {
        return user;
    }

    @NotNull
    public String getGPSUid() {
        return gps_uid;
    }

    @NotNull
    public GPSLocationModel getGPS() {
        return gps;
    }

    @NotNull
    public String getUid() {
        return uid;
    }

    @Override
    public Map<String, Object> toDto() {
        final Map<String, Object> dto = new HashMap<>();
        dto.put("user_uid", this.user_uid);
        dto.put("GPS_uid", this.gps_uid);
        return dto;
    }

    @Override
    public String getKey() {
        return getUid();
    }
}
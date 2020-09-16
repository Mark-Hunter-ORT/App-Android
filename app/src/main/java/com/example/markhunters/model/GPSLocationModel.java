package com.example.markhunters.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class GPSLocationModel extends Model {
    private String uid;
    private Double GPS_X;
    private Double GPS_Y;

    public GPSLocationModel(@NotNull final String uid, @NotNull final Double GPS_X, @NotNull final Double GPS_Y) {
        this.uid = uid;
        this.GPS_X = GPS_X;
        this.GPS_Y = GPS_Y;
    }

    @NotNull
    public Double getGPSX() {
        return GPS_X;
    }

    @NotNull
    public Double getGPSY() {
        return GPS_Y;
    }

    @NotNull
    public String getUid() {
        return uid;
    }

    @Override
    public Map<String, Object> toDto() {
        final Map<String, Object> dto = new HashMap<>();
        dto.put("GPS_X", this.GPS_X);
        dto.put("GPS_Y", this.GPS_Y);
        return dto;
    }

    @Override
    public String getKey() {
        return getUid();
    }
}
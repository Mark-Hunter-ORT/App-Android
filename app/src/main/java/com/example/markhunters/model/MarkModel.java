package com.example.markhunters.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class MarkModel extends Model {
    private String uid;
    private String user_uid;
    private UserModel user;
    private String category_uid;
    private CategoryModel category;
    private String location_uid;
    private LocationModel location;

    public MarkModel(@NotNull final String uid, @NotNull final UserModel user,
                     @NotNull final CategoryModel category, @NotNull final LocationModel location) {
        this.uid = uid;
        this.user_uid = user.getUid();
        this.user = user;
        this.category_uid = category.getUid();
        this.category = category;
        this.location_uid = location.getUid();
        this.location = location;
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
    public String getCategoryUid() {
        return category_uid;
    }

    @NotNull
    public CategoryModel getCategory() {
        return category;
    }

    @NotNull
    public String getLocationUid() {
        return location_uid;
    }

    @NotNull
    public LocationModel getLocation() {
        return location;
    }

    @NotNull
    public String getUid() {
        return uid;
    }

    @Override
    public Map<String, Object> toDto() {
        final Map<String, Object> dto = new HashMap<>();
        dto.put("user_uid", this.user_uid);
        dto.put("category_uid", this.category_uid);
        dto.put("location_uid", this.location_uid);
        return dto;
    }

    @Override
    public String getKey() {
        return getUid();
    }
}
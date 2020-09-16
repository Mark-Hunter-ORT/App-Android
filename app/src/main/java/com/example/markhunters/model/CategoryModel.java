package com.example.markhunters.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class CategoryModel extends Model {
    private String uid;
    private String name;

    public CategoryModel(@NotNull final String uid, @NotNull final String name) {
        this.uid = uid;
        this.name = name;
    }

    @NotNull
    public String getName() {
        return name;
    }

    @NotNull
    public String getUid() {
        return uid;
    }

    @Override
    public Map<String, Object> toDto() {
        final Map<String, Object> dto = new HashMap<>();
        dto.put("name", this.name);
        return dto;
    }

    @Override
    public String getKey() {
        return getUid();
    }
}
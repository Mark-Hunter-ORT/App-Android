package com.example.markhunters.dao;

import com.example.markhunters.model.Model;

import org.jetbrains.annotations.Nullable;

public interface DaoCallback <T extends Model> {
    void onCallback(@Nullable final T model);
}

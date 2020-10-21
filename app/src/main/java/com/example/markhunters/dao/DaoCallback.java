package com.example.markhunters.dao;

import com.example.markhunters.model.Model;

import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface DaoCallback <T extends Model> {
    default void onCallbackInstance(@Nullable final T model) {};
    default void onCallbackCollection(List<T> models) {};
}

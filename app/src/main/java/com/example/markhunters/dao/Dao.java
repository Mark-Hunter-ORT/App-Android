package com.example.markhunters.dao;

import org.jetbrains.annotations.NotNull;


public interface Dao <T> {
    void find(@NotNull final String key, DaoCallback<T> callback);
    void persist (@NotNull final T model, DaoCallback<T> callback);
    void create (@NotNull final T model, DaoCallback<T> callback);
    void update (@NotNull final T model, DaoCallback<T> callback);
}

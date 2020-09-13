package com.example.markhunters.dao;

import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;


public interface Dao <T> {
    void find(@NotNull final String key, DaoCallback<T> callback);
    void persist (@NotNull final T model, DaoCallback<T> callback);
    Task <Void> create (@NotNull final T model);
    Task <Void> update (@NotNull final T model);
}

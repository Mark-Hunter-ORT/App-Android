package com.example.markhunters.dao;

import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

public interface Dao <T> {
    T find(@NotNull final String key);
    Task <Void> persist (@NotNull final T model);
    Task <Void> create (@NotNull final T model);
    Task <Void> update (@NotNull final T model);
}

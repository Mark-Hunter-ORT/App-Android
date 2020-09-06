package com.example.markhunters.dao;

import org.jetbrains.annotations.NotNull;

public interface Dao <T> {
    T find(@NotNull final String key);
    T persist(@NotNull final T model);
}

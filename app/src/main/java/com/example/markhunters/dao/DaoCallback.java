package com.example.markhunters.dao;

import org.jetbrains.annotations.Nullable;

public interface DaoCallback <T> {
    void onCallback(@Nullable final T model);
}

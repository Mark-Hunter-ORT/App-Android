package com.example.markhunters.dao;

import com.example.markhunters.model.Model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


public abstract class Dao <T extends Model> {

    public void persist (@NotNull final T model, DaoCallback<T> callback) {
        find(model.getKey(), new DaoCallback<T>() {
            @Override
            public void onCallback(@Nullable final T persisted) {
                if (persisted != null) {
                    update(model, callback);
                } else {
                    create(model, callback);
                }
            }
        });
    }

    public abstract void find (@NotNull final String key, DaoCallback<T> callback);
    protected abstract void create (@NotNull final T model, DaoCallback<T> callback);
    protected abstract void update (@NotNull final T model, DaoCallback<T> callback);
}

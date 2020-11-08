package com.example.markhunters.service.dao;

import com.example.markhunters.model.Model;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.service.rest.RestClientCallbacks;
import com.example.markhunters.service.rest.RestClientCallbacks.CallbackInstance;
import com.example.markhunters.service.rest.RestClientCallbacks.CallbackInstance2;

import org.jetbrains.annotations.NotNull;


public abstract class Dao <T extends Model> {

    public void persist (@NotNull final T model, CallbackInstance2<T> callback) {
        UserModel uModel = (UserModel) model;
        find(uModel.getKey(), persisted -> {
            if (persisted != null) {
                update(model, callback);
            } else {
                create(model, callback);
            }
        });
    }

    public abstract void find (@NotNull final String key, CallbackInstance2<T> callback);
    protected abstract void create (@NotNull final T model, CallbackInstance2<T> callback);
    protected abstract void update (@NotNull final T model, CallbackInstance2<T> callback);
}

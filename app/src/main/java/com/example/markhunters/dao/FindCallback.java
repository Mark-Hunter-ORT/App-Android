package com.example.markhunters.dao;

import com.example.markhunters.model.UserModel;

public interface FindCallback<T> {
    void onFindCallback(T model);
}

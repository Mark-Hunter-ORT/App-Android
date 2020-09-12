package com.example.markhunters.dao;

import com.example.markhunters.model.UserModel;


public class DaoProvider {
    private static Dao<UserModel> userDao = null;

    public static Dao<UserModel> getUserDao() {
        if (userDao == null) {
            userDao = new FirebaseUserDao();
        }
        return userDao;
    }
}

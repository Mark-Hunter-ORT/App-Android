package com.example.markhunters.service;

import com.example.markhunters.BuildConfig;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.service.dao.Dao;
import com.example.markhunters.service.dao.FirebaseUserDao;
import com.example.markhunters.service.rest.RestAPIClient;
import com.example.markhunters.service.rest.RestClient;
import com.example.markhunters.service.rest.RestMOCKClient;

import org.jetbrains.annotations.NotNull;


public class ServiceProvider {
    private static RestClient restClient = null;
    private static Dao<UserModel> userDao = null;

    public static Dao<UserModel> getUserDao() {
        if (userDao == null) {
            userDao = new FirebaseUserDao();
        }
        return userDao;
    }

    public static RestClient getRestClient(@NotNull String token) {
        if (restClient == null || !restClient.isTokenValid(token)) {
            restClient = initRestClient(token);
        }
        return restClient;
    }

    private static RestClient initRestClient(String token) {
        if (Boolean.parseBoolean(BuildConfig.RESTClientMOCK)) {
            return new RestMOCKClient(token);
        } else {
            return new RestAPIClient(token);
        }
    }
}

package com.example.markhunters.service;

import com.example.markhunters.model.UserModel;
import com.example.markhunters.service.rest.RestClient;

import org.jetbrains.annotations.NotNull;


public class ServiceProvider {
    private static RestClient restClient = null;

    public static RestClient getRestClient(@NotNull String token) {
        if (restClient == null || !restClient.isTokenValid(token)) {
            restClient = initRestClient(token);
        }
        return restClient;
    }

    private static RestClient initRestClient(String token) {
        return new RestClient(token);
    }
}

package com.example.markhunters.service.rest;

import org.jetbrains.annotations.NotNull;

public abstract class RestClient {
    private final String token;
    protected RestClient (@NotNull String token) {
        this.token = token;
    }

    public boolean isTokenValid(String token) {
        return this.token.equals(token);
    }
}

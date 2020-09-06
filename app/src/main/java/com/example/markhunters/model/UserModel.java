package com.example.markhunters.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;

public class UserModel implements Serializable {
    private String google_id;
    private String username;
    private String email;

    public UserModel (@NotNull final String google_id, @NotNull final String username, @NotNull final String email) {
        this.google_id = google_id;
        this.username = username;
        this.email = email;
    }

    @NotNull
    public String getEmail() {
        return email;
    }

    @NotNull
    public String getUsername() {
        return username;
    }

    public void setUsername(@NotNull final String username) {
        this.username = username;
    }

    @NotNull
    public String getGoogle_id() {
        return google_id;
    }
}

package com.example.markhunters.model;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

public class UserModel extends Model {
    private String uid;
    private String nickname;
    private String email;
    private List<String> following = new ArrayList<>();
    private Boolean followingLoaded;

    public UserModel(@NotNull final String uid, @NotNull final String nickname, @NotNull final String email) {
        this.uid = uid;
        this.nickname = nickname;
        this.email = email;
        this.followingLoaded = false;
    }

    private UserModel(@NotNull final String uid, @NotNull final String email) {
        this.uid = uid;
        this.email = email;
        this.followingLoaded = false;
    }

    /**
     * This method is invoked when a new user signs in. Thus, nickname must be null.
     */
    public static UserModel createNew(@NotNull final String uid, @NotNull final String email) {
        return new UserModel(uid, email);
    }

    @NotNull
    public String getEmail() {
        return email;
    }

    @Nullable
    public String getNickname() {
        return nickname;
    }

    @NotNull
    public String getUid() {
        return uid;
    }

    @Override
    public Map<String, Object> toDto() {
        final Map<String, Object> dto = new HashMap<>();
        dto.put("nickname", this.nickname);
        dto.put("email", this.email);
        if(this.followingLoaded) dto.put("following", this.following);
        return dto;
    }

    @Override
    public String getKey() {
        return getUid();
    }
}

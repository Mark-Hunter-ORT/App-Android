package com.example.markhunters.model;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class UserModel implements Serializable {
    private String uid;
    private String nickname;
    private String email;

    public UserModel (@NotNull final String uid, @NotNull final String nickname, @NotNull final String email) {
        this.uid = uid;
        this.nickname = nickname;
        this.email = email;
    }

    @NotNull
    public String getEmail() {
        return email;
    }

    @NotNull
    public String getNickname() {
        return nickname;
    }

    public void setNickname(@NotNull final String nickname) {
        this.nickname = nickname;
    }

    @NotNull
    public String getUid() {
        return uid;
    }

    public Object buildDTO() {
        final Map<String, Object> dto = new HashMap<>();
        dto.put("nickname", this.nickname);
        dto.put("email", this.email);
        return dto;
    }
}

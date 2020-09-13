package com.example.markhunters.model;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class UserModel extends Model {
    private String uid;
    private String nickname;
    private String email;

    public UserModel(@NotNull final String uid, @NotNull final String nickname, @NotNull final String email) {
        this.uid = uid;
        this.nickname = nickname;
        this.email = email;
    }

    private UserModel(@NotNull final String uid, @NotNull final String email) {
        this.uid = uid;
        this.email = email;
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
        return dto;
    }
}

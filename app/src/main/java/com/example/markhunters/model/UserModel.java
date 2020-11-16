package com.example.markhunters.model;

import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

public class UserModel extends Model {
    private String nickname;
    private String email;
    private String displayName;

    public void setFirebaseData(FirebaseUser firebaseUser) {
        this.displayName = firebaseUser.getDisplayName();
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject();
    }

    private UserModel(@NotNull final String nickname, @NotNull final String email) {
        this.nickname = nickname;
        this.email = email;
    }

    @NotNull
    public String getEmail() {
        return email;
    }

    @Nullable
    public String getNickname() {
        return nickname;
    }

    public String getDisplayName() {
        return displayName;
    }

    public static UserModel fromJson(JSONObject json) {
        UserModel user = null;
        try {
            user = new UserModel(json.getString("username"),
                    json.getString("email"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }
}

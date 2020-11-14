package com.example.markhunters.model;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.auth.User;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserModel extends Model {
    private String uid;
    private String nickname;
    private String email;
    private String photoStringUri;
    private String displayName;

    public UserModel(@NotNull final String uid, @NotNull final String nickname, @NotNull final String email) {
        this.uid = uid;
        this.nickname = nickname;
        this.email = email;
    }

    public UserModel(String uid, String nickname, String email, String displayName, String photoStringUri) {
        this.uid = uid;
        this.nickname = nickname;
        this.email = email;
        this.photoStringUri = photoStringUri;
        this.displayName = displayName;
    }

    public void setFirebaseData(FirebaseUser firebaseUser) {
        this.photoStringUri = firebaseUser.getPhotoUrl().getPath();
        this.displayName = firebaseUser.getDisplayName();
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject();
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

    public Map<String, Object> toDto() {
        final Map<String, Object> dto = new HashMap<>();
        dto.put("nickname", this.nickname);
        dto.put("email", this.email);
        return dto;
    }

    public String getKey() {
        return getUid();
    }

    public String getPhotoStringUri() {
        return photoStringUri;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Uri getPhotoUri () {
        return Uri.parse(photoStringUri);
    }

    public static UserModel fromJson(JSONObject json) {
        UserModel user = null;
        try {
            user = new UserModel(json.getString("uid"), json.getString("username"),
                    json.getString("email"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }
}

package com.example.markhunters.model;

import android.net.Uri;

import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class UserModel extends Model {
    private String id;
    private String email;
    private String photoStringUri;
    private String displayName;

    public UserModel(@NotNull final String id, @NotNull final String nickname, @NotNull final String email) {
        this.id = id;
        this.email = email;
    }

    public UserModel(String id, String nickname, String email, String displayName, String photoStringUri) {
        this.id = id;
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

    private UserModel(@NotNull final String id, @NotNull final String email) {
        this.id = id;
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

    @NotNull
    public String getId() {
        return id;
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
}

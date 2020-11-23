package com.example.markhunters.model;

import com.google.firebase.auth.FirebaseUser;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserModel extends Model {
    private String nickname;
    private String email;
    private String displayName;
    private List<String> followings;

    public void setFirebaseData(FirebaseUser firebaseUser) {
        this.displayName = firebaseUser.getDisplayName();
    }

    @Override
    public JSONObject toJson() {
        return new JSONObject();
    }

    public UserModel(@NotNull final String nickname, @NotNull final String email) {
        this.nickname = nickname;
        this.email = email;
        followings = new ArrayList<>();
        mockFollowing();
    }

    private void mockFollowing() {
        followings.add("1");
        followings.add("2");
        followings.add("3");
    }

    public void addFollowing(String uid) {
        this.followings.add(uid);
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
            JSONArray userFollowings = json.getJSONArray("following");
            for (int i = 0; i < userFollowings.length(); i ++) {
                user.addFollowing(userFollowings.getString(i));
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return user;
    }

    public int getFollowers() {
        return followings.size();
    }

    public void removeFollowing(String id) {
        followings.remove(id);
    }
}

package com.example.markhunters.model;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class UserFollowing extends Model{
    private String uid;
    private String username;

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public UserFollowing(@NotNull String uid, @NotNull String username){
        this.uid = uid;
        this.username = username;
    }

    public static UserFollowing fromJson(JSONObject json){
        UserFollowing user = null;
        try {
            user = new UserFollowing(json.getString("uid"), json.getString("username"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return user;
    }

    public static List<UserFollowing> fromJsonArray(JSONArray jsonArray){
        final List<UserFollowing> followings = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i ++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                followings.add(fromJson(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return followings;
    }

    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("uid", this.uid);
        json.put("username", this.username);
        return json;
    }
}

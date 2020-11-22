package com.example.markhunters.service.rest;

import org.jetbrains.annotations.NotNull;

import android.location.Location;
import android.util.Log;

import com.example.markhunters.fragments.MarkCreationFragment;
import com.example.markhunters.model.Category;
import com.example.markhunters.model.MarkLocation;
import com.example.markhunters.model.Mark;
import com.example.markhunters.model.UserFollowing;
import com.example.markhunters.model.UserModel;
import com.example.markhunters.service.rest.RestClientCallbacks.CallbackAction;
import com.example.markhunters.service.rest.RestClientCallbacks.CallbackCollection;
import com.example.markhunters.service.rest.RestClientCallbacks.CallbackInstance;
import com.google.firebase.firestore.auth.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RestClient {
    private final String SERVER_FQDN = "http://18.229.104.197:5000/";
    private final String CATEGORIES = "api/category/";
    private final String CATEGORY = "api/category/<id>/";
    private final String LOCATIONS = "api/location/";
    private final String LOCATION = "api/location/<id>/";
    private final String MARKS = "api/mark/";
    private final String MARK = "api/mark/<id>/";
    private final String MARKS_DISTANCE = "api/mark/<lat>/<lon>/<distance>/";
    private final String USER = "api/user/<id>/";
    private final String USER_POST = "api/user/";
    private final String USER_FOLLOWINGS = "api/user/followings/";
    private final String USER_FOLLOW = "api/user/<id>/follow/";
    private final String USER_UNFOLLOW = "api/user/<id>/unfollow/";
    private final MediaType MEDIA = MediaType.parse("application/json; charset=utf-8");

    private String token;
    private OkHttpClient httpclient;
    private Response response;

    public RestClient (@NotNull String token) {
        this.token = token;
        this.httpclient = new OkHttpClient();
    }

    public boolean isTokenValid(String token) {
        return this.token.equals(token);
    }

    private void doSomething(Response response) throws IOException {
        assert response.body() != null;
        Log.e("test", response.body().string());
    }

    public void getCaterogy(String id, CallbackInstance<Category> callback) {
        String url = this.SERVER_FQDN + this.CATEGORY.replace("<id>", id);
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    callback.onSuccess(new Category(response.body().string()));
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void getCategories(CallbackCollection<Category> callback) {
        String url = this.SERVER_FQDN + this.CATEGORIES;
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    try {
                        JSONArray categoryJson = new JSONArray(response.body().string());
                        callback.onSuccess(Category.fromJsonArray(categoryJson));
                    } catch (JSONException e) {
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void getLocations(CallbackCollection<MarkLocation> callback) {
        String url = this.SERVER_FQDN + this.LOCATIONS;
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    try {
                        JSONArray locationJson = new JSONArray(response.body().string());
                        callback.onSuccess(MarkLocation.fromJsonArray(locationJson));
                    } catch (JSONException e) {
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void postLocation(MarkLocation markLocation, CallbackInstance<MarkLocation> callback) {
        JSONObject json = markLocation.toJson();
        RequestBody reqBody = RequestBody.create(MEDIA, json.toString());
        String url = this.SERVER_FQDN + this.LOCATIONS;
        final Request request = new Request.Builder()
                .url(url)
                .post(reqBody)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject locationJson = new JSONObject(response.body().string());
                        callback.onSuccess(MarkLocation.fromJson(locationJson));
                    } catch (JSONException e) {
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void getLocation(String id, CallbackInstance<MarkLocation> callback) {
        String url = this.SERVER_FQDN + this.LOCATION.replace("<id>", id);
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    try {
                        JSONObject locationJson = new JSONObject(response.body().string());
                        callback.onSuccess(MarkLocation.fromJson(locationJson));
                    } catch (JSONException e) {
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void getMarks(CallbackCollection<Mark> callback) {
        String url = this.SERVER_FQDN + this.MARKS;
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        List<Mark> marks = Mark.fromJsonArray(jsonArray);
                        callback.onSuccess(marks);
                    } catch (JSONException e) {
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void getMarksByDistance(Location loc, Double distance, CallbackCollection<Mark> callback) {
        String url = this.SERVER_FQDN + this.MARKS_DISTANCE.replace(
                "<lon>", String.valueOf(loc.getLatitude())).replace(
                "<lat>", String.valueOf(loc.getLongitude())).replace(
                "<distance>", distance.toString());
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        List<Mark> marks = Mark.fromJsonArray(jsonArray);
                        callback.onSuccess(marks);
                    } catch (JSONException e) {
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void postMark (Mark mark, CallbackAction callback) {
        JSONObject json = mark.toJson();
        RequestBody reqBody = RequestBody.create(MEDIA, json.toString());
        String url = this.SERVER_FQDN + this.MARKS;
        final Request request = new Request.Builder()
                .url(url)
                .post(reqBody)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) {
                if (response.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onError(response.message(), response.code());
                }
            }
        });
    }

    public void getMark(String id, CallbackInstance<Mark> callback) {
        String url = this.SERVER_FQDN + this.MARK.replace("<id>", id);
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    try {
                        JSONObject markJson = new JSONObject(response.body().string());
                        callback.onSuccess(Mark.fromJson(markJson));
                    } catch (JSONException e) {
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void getUser(String id, CallbackInstance<UserModel> callback) {
        String url = this.SERVER_FQDN + this.USER.replace("<id>", id);
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    try {
                        JSONObject userJson = new JSONObject(response.body().string());
                        UserModel user = UserModel.fromJson(userJson);
                        callback.onSuccess(user);
                    } catch (JSONException e) {
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    callback.onError(response.message(), response.code());
                }
            }
        });
    }

    public void postUser(String username, CallbackInstance<UserModel> callback) {
        String json = "{\"username\":\"<username>\"}".replace("<username>", username);
        RequestBody reqBody = RequestBody.create(MEDIA, json);
        String url = this.SERVER_FQDN + this.USER_POST;
        final Request request = new Request.Builder()
                .url(url)
                .post(reqBody)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    try {
                        JSONObject userJson = new JSONObject(response.body().string());
                        UserModel user = UserModel.fromJson(userJson);
                        callback.onSuccess(user);
                    } catch (JSONException e) {
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void followUser(String id, CallbackAction callback) {
        RequestBody reqbody = RequestBody.create(null, new byte[0]);
        String url = this.SERVER_FQDN + this.USER.replace("<id>", id);
        final Request request = new Request.Builder()
                .url(url)
                .post(reqbody)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    doSomething(response);
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void unfollowUser(String id, CallbackAction callback) {
        String url = this.SERVER_FQDN + this.USER.replace("<id>", id);
        final Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    doSomething(response);
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }

    public void getFollowings(CallbackCollection<UserFollowing> callback){
        String url = this.SERVER_FQDN + this.USER_FOLLOWINGS;
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        List<UserFollowing> followings = UserFollowing.fromJsonArray(jsonArray);
                        callback.onSuccess(followings);
                    } catch (JSONException e) {
                        callback.onFailure(e.getMessage());
                    }
                } else {
                    callback.onFailure(response.message());
                }
            }
        });
    }
}

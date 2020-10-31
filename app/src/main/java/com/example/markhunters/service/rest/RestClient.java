package com.example.markhunters.service.rest;

import org.jetbrains.annotations.NotNull;
import android.util.Log;

import com.example.markhunters.model.MarkLocation;
import com.example.markhunters.model.Mark;
import com.example.markhunters.service.rest.RestClientCallbacks.CallbackCollection;

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
    private final String USER = "api/user/<id>/";
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

    public void getCaterogy(String id) {
        String url = this.SERVER_FQDN + this.CATEGORY.replace("<id>", id);
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    doSomething(response);
                }
            }
        });
    }

    public void getCategories() {
        String url = this.SERVER_FQDN + this.CATEGORIES;
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    doSomething(response);
                }
            }
        });
    }

    public void getLocations() {
        String url = this.SERVER_FQDN + this.LOCATIONS;
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    doSomething(response);
                }
            }
        });
    }

    public void postLocation(MarkLocation markLocation) {
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
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    doSomething(response);
                }
            }
        });
    }

    public void getLocation(String id) {
        String url = this.SERVER_FQDN + this.LOCATION.replace("<id>", id);
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    doSomething(response);
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
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    try {
                        JSONArray jsonArray = new JSONArray(response.body().string());
                        List<Mark> marks = Mark.fromJSONArray(jsonArray);
                        callback.onCallback(marks);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void postMark(Mark mark) {
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
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    doSomething(response);
                }
            }
        });
    }

    public void getMark(String id) {
        String url = this.SERVER_FQDN + this.MARK.replace("<id>", id);
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    doSomething(response);
                }
            }
        });
    }

    public void getUser(String id) {
        String url = this.SERVER_FQDN + this.USER.replace("<id>", id);
        final Request request = new Request.Builder()
                .url(url)
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    doSomething(response);
                }
            }
        });
    }

    public void followUser(String id) {
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
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    doSomething(response);
                }
            }
        });
    }

    public void unfollowUser(String id) {
        String url = this.SERVER_FQDN + this.USER.replace("<id>", id);
        final Request request = new Request.Builder()
                .url(url)
                .delete()
                .addHeader("User-Token", this.token)
                .build();
        this.httpclient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if(response.isSuccessful()) {
                    doSomething(response);
                }
            }
        });
    }
}

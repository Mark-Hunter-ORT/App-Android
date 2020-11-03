package com.example.markhunters.model;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Category extends Model {
    private Integer id;
    private String name;

    public Category (Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(String name){
        this.name = name;
    }

    public Integer getId() { return this.id; }

    public String getName() { return this.name; }

    @Override
    public JSONObject toJson() throws JSONException {
        JSONObject json = new JSONObject();
        json.put("name", this.name);
        if (this.id != null) {
            json.put("id", this.id);
        }
        return json;
    }

    public static List<Category> fromJsonArray(JSONArray json) {
        final List<Category> categories = new ArrayList<>();

        for (int i = 0; i < json.length(); i ++) {
            try {
                categories.add(new Category(json.getString(i)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return categories;
    }
}

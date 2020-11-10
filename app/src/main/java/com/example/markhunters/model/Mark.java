package com.example.markhunters.model;

import com.google.android.gms.maps.model.LatLng;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Mark extends Model {
    public Integer id;
    public String userId;
    public String category;
    public MarkLocation location;
    public Content content;

    public Mark(@NotNull String userId, @NotNull String category, @NotNull MarkLocation location, @NotNull Content content){
        this.userId = userId;
        this.category = category;
        this.location = location;
        this.content = content;
    }

    public Mark(String userId, String category, MarkLocation location, Content content, Integer id){
        this.userId = userId;
        this.category = category;
        this.location = location;
        this.content = content;
        this.id = id;
    }

    // todo estos dos hay que dejarlos hasta que matemos a FirebaseUserDao
    public Map<String, Object> toDto() {
        return null;
    }

    public String getKey() {
        return null;
    }

    public JSONObject toJson(){
        JSONObject json = new JSONObject();
        try {
            json.put("category", this.category);
            json.put("location", this.location.toJson());
            JSONObject content = new JSONObject();
            content.put("text", this.content.text);
            JSONArray images = new JSONArray();
            if(this.content.getHasImages()){
                for ( String image: this.content.images) {
                    images.put(image);
                }
            }
            content.put("files", images);
            json.put("content", content);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return json;
    }

    public Mark(String category, MarkLocation location, Content content){
        this.category = category;
        this.location = location;
        this.content = content;
    }

    public Mark(String category, MarkLocation location, Content content, Integer id){
        this.category = category;
        this.location = location;
        this.content = content;
        this.id = id;
    }

    public static Mark fromJson(JSONObject json){
        Mark mark = null;
        try {
            ArrayList<String> images = new ArrayList<>();
            JSONArray jsonImages = json.getJSONObject("content").getJSONArray("images");
            for (int i=0; i<jsonImages.length(); i++) {
                images.add(jsonImages.getString(i));
            }
            mark = new Mark(json.getString("user_id"),
                    json.getString("category"),
                    MarkLocation.fromJson(json.getJSONObject("location")),
                    new Content(json.getJSONObject("content").getString("text"),
                            images),
                    json.getInt("id"));
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mark;
    }

    public static List<Mark> fromJsonArray(JSONArray jsonArray) {
        final List<Mark> marks = new ArrayList<>();

        for (int i = 0; i < jsonArray.length(); i ++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                marks.add(fromJson(jsonObject));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        return marks;
    }

    public LatLng getLatLng() {
        return location.getLatLng();
    }

    public String getTitle() {
        return content.getText();
    }
}

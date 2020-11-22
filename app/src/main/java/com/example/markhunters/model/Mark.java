package com.example.markhunters.model;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Mark extends Model {
    public Integer id;
    public String userId;
    public String userName;
    public String category;
    public MarkLocation location;
    public Content content;
    public Boolean isByFollowed;

    // Este es el mark que viene del server
    public Mark(String userId, String category, MarkLocation location, Content content, Integer id, Boolean isByFollowed, String userName){
        this.userId = userId;
        this.userName = userName;
        this.category = category;
        this.location = location;
        this.content = content;
        this.id = id;
        this.isByFollowed = isByFollowed;
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

    // Esto es para Marks nuevos, todavía no tienen id ni uid
    public Mark(String category, MarkLocation location, Content content){
        this.category = category;
        this.location = location;
        this.content = content;
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
                    json.getInt("id"), json.getBoolean("by_followed"),
                    json.getString("username"));
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

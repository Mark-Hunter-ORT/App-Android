package com.example.markhunters.model;

import java.util.ArrayList;

public class Content {
    public Integer id;
    public String text;
    public ArrayList<String> images;
    private Boolean hasImages;

    public Content(String text){
        this.text = text;
        this.hasImages = false;
    }

    public Content(String text, Integer id){
        this.text = text;
        this.id = id;
        this.hasImages = false;
    }

    public Content(String text, ArrayList<String> images){
        this.text = text;
        this.images = new ArrayList<>();
        for (String image: images) {
            this.images.add(image);
        }
        this.hasImages = true;
    }

    public Content(String text, ArrayList<String> images, Integer id){
        this.text = text;
        this.id = id;
        this.images = new ArrayList<>();
        for (String image: images) {
            this.images.add(image);
        }
        this.hasImages = true;
    }

    public Boolean getHasImages(){
        return this.hasImages;
    }

    public String getText() {
        return text;
    }
}

package com.cgit.ogdensuntamedwomen.model;

import java.io.Serializable;

public class Places implements Serializable {
    //ID,Name,Description,Image,lat,long,activeDistance
    private String id;
    private String title;
    private String description;
    private String image;
    private String lang;
    private String lng;
    private String distance;

    public Places() {
    }

    public Places(String id, String title, String description, String image, String lang, String lng, String distance) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.lang = lang;
        this.lng = lng;
        this.distance = distance;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }
}

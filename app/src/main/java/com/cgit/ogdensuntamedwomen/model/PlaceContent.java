package com.cgit.ogdensuntamedwomen.model;

public class PlaceContent {
    private String location;
    private String title;
    private String description;
    private String audio;

    public PlaceContent() {
    }

    public PlaceContent(String location, String title, String description, String audio) {
        this.location = location;
        this.title = title;
        this.description = description;
        this.audio = audio;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }
}

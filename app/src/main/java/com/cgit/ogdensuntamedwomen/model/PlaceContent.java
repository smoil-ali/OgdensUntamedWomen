package com.cgit.ogdensuntamedwomen.model;

public class PlaceContent {
    private String location;
    private String title;
    private String type;
    private String description;
    private String audio;
    private boolean isPlay;
    private boolean isPlaying;

    public boolean isPlaying() {
        return isPlaying;
    }

    public void setPlaying(boolean playing) {
        isPlaying = playing;
    }

    public PlaceContent() {
    }

    public PlaceContent(String location, String title,String type, String description) {
        this.location = location;
        this.title = title;
        this.description = description;
        this.type=type;

    }

    public PlaceContent(String location, String title,String type, String description, String audio) {
        this.location = location;
        this.title = title;
        this.description = description;
        this.audio = audio;
        this.type=type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isPlay() {
        return isPlay;
    }

    public void setPlay(boolean play) {
        isPlay = play;
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

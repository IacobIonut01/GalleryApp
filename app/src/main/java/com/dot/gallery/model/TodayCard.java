package com.dot.gallery.model;

public class TodayCard {

    private String path, name, album, timestamp;

    public TodayCard() {

    }

    public TodayCard(String path, String name, String album, String timestamp) {
        this.path = path;
        this.name = name;
        this.album = album;
        this.timestamp = timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getAlbum() {
        return album;
    }

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

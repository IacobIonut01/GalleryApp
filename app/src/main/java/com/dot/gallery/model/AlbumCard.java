package com.dot.gallery.model;

public class AlbumCard {

    private String path, name, count, timestamp;

    public AlbumCard() {

    }

    public AlbumCard(String path, String name, String count, String timestamp) {
        this.path = path;
        this.name = name;
        this.count = count;
        this.timestamp = timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
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

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}

package com.dot.gallery.model;

public class MediaCard  {

    public String path, name, album, timestamp;

    public MediaCard() {

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

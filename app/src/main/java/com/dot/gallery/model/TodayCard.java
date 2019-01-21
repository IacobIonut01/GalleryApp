package com.dot.gallery.model;

public class TodayCard extends MediaCard {

    public TodayCard(String path, String album, String timestamp) {
        this.path = path;
        this.album = album;
        this.timestamp = timestamp;
        setMediaType(0);
    }
}

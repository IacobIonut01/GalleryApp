package com.dot.gallery.model;

public class VideoCard extends MediaCard {

    public VideoCard(String path, String timestamp, String thumb) {
        this.path = path;
        this.timestamp = timestamp;
        this.thumbnail = thumb;
        setMediaType(1);
    }
}

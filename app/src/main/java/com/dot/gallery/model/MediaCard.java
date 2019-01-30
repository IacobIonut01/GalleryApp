package com.dot.gallery.model;

public class MediaCard  {

    public String path, album, timestamp, thumbnail;
    public int mediaType = 0;
    public boolean isSelected, isVideo;
    /*
    * 0 - Images
    * 1 - Videos
    * 2 - Gif
    * */

    public MediaCard() {

    }

    public MediaCard(String path, String album, String timestamp) {
        this.path = path;
        this.album = album;
        this.timestamp = timestamp;
        this.isVideo = false;
        setMediaType(0);
    }

    public MediaCard(String path, String timestamp, String thumb, boolean isVideo) {
        this.path = path;
        this.timestamp = timestamp;
        this.thumbnail = thumb;
        this.isVideo = isVideo;
        setMediaType(1);
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getMediaType() {
        return mediaType;
    }

    public void setMediaType(int mediaType) {
        this.mediaType = mediaType;
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

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }
}

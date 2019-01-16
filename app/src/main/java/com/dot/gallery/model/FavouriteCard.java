package com.dot.gallery.model;

public class FavouriteCard {

    private String path, timestamp;
    private boolean isFirst;

    public FavouriteCard() {

    }

    public FavouriteCard(String path, String name, String timestamp) {
        this.path = path;
        this.timestamp = timestamp;
    }

    public FavouriteCard(boolean isFirst) {
        this.isFirst = isFirst;
    }

    public boolean isFirst() {
        return isFirst;
    }

    public void setFirst(boolean first) {
        isFirst = first;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}

package com.dot.gallery.model;

import android.view.View;

public class PickerCard {

    private String path, name, album, timestamp;
    private View.OnClickListener lst;

    public PickerCard() {

    }

    public PickerCard(String path, String name, String album, String timestamp, View.OnClickListener lst) {
        this.path = path;
        this.name = name;
        this.album = album;
        this.timestamp = timestamp;
        this.lst = lst;
    }

    public View.OnClickListener getLst() {
        return lst;
    }

    public void setLst(View.OnClickListener lst) {
        this.lst = lst;
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

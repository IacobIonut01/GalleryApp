package com.dot.gallery.model;

import android.view.View;

import com.google.android.material.button.MaterialButton;

import java.util.List;

public class PickerCard {

    private String path, name, album, timestamp, remotePath, remoteTimestamp;
    private boolean isSelected = false;
    private MaterialButton btn;
    private List<FavouriteCard> selected;


    public PickerCard() {

    }

    public PickerCard(String path, String name, String album, String timestamp, MaterialButton btn, List<FavouriteCard> selected, String remotePath, String remoteTimestamp) {
        this.path = path;
        this.name = name;
        this.album = album;
        this.timestamp = timestamp;
        this.btn = btn;
        this.selected = selected;
        this.remotePath = remotePath;
        this.remoteTimestamp = remoteTimestamp;
    }

    public void setSelected(List<FavouriteCard> selected) {
        this.selected = selected;
    }

    public List<FavouriteCard> getSelected() {
        return selected;
    }

    public MaterialButton getBtn() {
        return btn;
    }

    public String getRemotePath() {
        return remotePath;
    }

    public String getRemoteTimestamp() {
        return remoteTimestamp;
    }

    public void setBtn(MaterialButton btn) {
        this.btn = btn;
    }

    public void setRemotePath(String remotePath) {
        this.remotePath = remotePath;
    }

    public void setRemoteTimestamp(String remoteTimestamp) {
        this.remoteTimestamp = remoteTimestamp;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
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

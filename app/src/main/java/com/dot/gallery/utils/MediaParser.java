package com.dot.gallery.utils;

import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;

import com.dot.gallery.model.MediaCard;
import com.dot.gallery.model.TodayCard;
import com.dot.gallery.model.VideoCard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

public class MediaParser extends AsyncTask<String, Void, String> {

    AppCompatActivity activity;
    FragmentActivity factivity;
    List<MediaCard> allList = new ArrayList<>();

    public void setActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        allList.clear();
    }

    @Override
    protected String doInBackground(String... strings) {
        /*String xml = "";

        Cursor cursor;
        int column_index_data, vcolumn_index_data, column_index_album, column_index_timestamp, vcolumn_index_timestamp, vcolumn_index_thumbnail;
        String absolutePathOfImage, album, thumb, vpath;
        int timestamp, vtimestamp;
        final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

        String[] projection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.TITLE,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                MediaStore.MediaColumns.DATE_MODIFIED};
        String[] vprojection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DATE_MODIFIED,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Thumbnails.DATA};

        Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri uriInternal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        Uri vuriExternal = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Uri vuriInternal = MediaStore.Video.Media.INTERNAL_CONTENT_URI;

        Cursor cursorExternal = activity.getContentResolver().query(uriExternal, projection, "bucket_display_name = \""+album_track+"\"",
                null, orderBy + " DESC");
        Cursor cursorInternal = activity.getContentResolver().query(uriInternal, projection, "bucket_display_name = \""+album_track+"\"",
                null, orderBy + " DESC");
        Cursor cursorVExternal = activity.getContentResolver().query(vuriExternal, vprojection, "bucket_display_name = \""+album_track+"\"",
                null, orderBy + " DESC");
        Cursor cursorVInternal = activity.getContentResolver().query(vuriInternal, vprojection, "bucket_display_name = \""+album_track+"\"",
                null, orderBy + " DESC");
        cursor = new MergeCursor(new Cursor[]{cursorExternal,cursorInternal});
        Cursor vcursor = new MergeCursor(new Cursor[]{cursorVExternal,cursorVInternal});

        column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        column_index_album = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
        column_index_timestamp = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED);
        vcolumn_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        vcolumn_index_timestamp = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED);
        vcolumn_index_thumbnail = cursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
        while (cursor.moveToNext() || vcursor.moveToNext()) {
            absolutePathOfImage = cursor.getString(column_index_data);
            album = cursor.getString(column_index_album);
            timestamp = cursor.getInt(column_index_timestamp);
            File checkPath = new File(absolutePathOfImage);
            if (checkPath.exists()) {
                allList.add(new TodayCard(absolutePathOfImage, album, String.valueOf(timestamp)));
            }
            if (vcursor.moveToNext()) {
                vpath = vcursor.getString(vcolumn_index_data);
                thumb = vcursor.getString(vcolumn_index_thumbnail);
                vtimestamp = vcursor.getInt(vcolumn_index_timestamp);
                File vcheckPath = new File(vpath);
                if (vcheckPath.exists()) {
                    allList.add(new VideoCard(vpath, String.valueOf(vtimestamp), thumb));
                }
            }
        }
        allList.sort((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));*/
        return null;
    }

}

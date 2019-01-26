package com.dot.gallery.utils;

import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;

import com.dot.gallery.R;
import com.dot.gallery.adapters.TodayAdapter;
import com.dot.gallery.model.MediaCard;
import com.dot.gallery.model.TodayCard;
import com.dot.gallery.model.VideoCard;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MediaParser {

    AppCompatActivity activity;
    FragmentActivity factivity;
    RecyclerView.LayoutManager lm;
    @RecyclerView.Orientation int orientation;
    RecyclerView recycler;
    TextView title;
    List<MediaCard> list = new ArrayList<>();
    boolean getRecents = false;

    public void initActivity(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setTitle(int resID) {
        title = activity.findViewById(resID);
    }

    public void setTitle(TextView textView) {
        title = textView;
    }

    public void setLayoutManager(RecyclerView.LayoutManager lm) {
        this.lm = lm;
    }

    public void setLayoutOrientation(@RecyclerView.Orientation int orientation) {
        this.orientation = orientation;
    }


    private class parseUsingActivity extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";
            int currentStamp = (int) TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis());
            int yesterdayStamp = (int) TimeUnit.MILLISECONDS.toSeconds((System.currentTimeMillis() - 24 * 60 * 60 * 1000));

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

            Cursor cursorExternal = activity.getContentResolver().query(uriExternal, projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, orderBy + " DESC");
            Cursor cursorInternal = activity.getContentResolver().query(uriInternal, projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, orderBy + " DESC");
            Cursor cursorVExternal = activity.getContentResolver().query(vuriExternal, vprojection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, orderBy + " DESC");
            Cursor cursorVInternal = activity.getContentResolver().query(vuriInternal, vprojection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
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
                if (currentStamp >= timestamp && timestamp >= yesterdayStamp && checkPath.exists()) {
                    list.add(new TodayCard(absolutePathOfImage, album, String.valueOf(timestamp)));
                }
                if (vcursor.moveToNext()) {
                    vpath = vcursor.getString(vcolumn_index_data);
                    thumb = vcursor.getString(vcolumn_index_thumbnail);
                    vtimestamp = vcursor.getInt(vcolumn_index_timestamp);
                    File vcheckPath = new File(vpath);
                    if (currentStamp >= vtimestamp && vtimestamp >= yesterdayStamp && vcheckPath.exists()) {
                        list.add(new VideoCard(vpath, String.valueOf(vtimestamp), thumb));
                    }
                }
            }
            list.sort((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            TodayAdapter adapter = new TodayAdapter(activity, list);
            recycler.setAdapter(adapter);
            if (lm != null)
                recycler.setLayoutManager(lm);
            else
                recycler.setLayoutManager(new LinearLayoutManager(activity, orientation, false));
            if (list.size() == 0)
                title.setVisibility(View.GONE);
            else
                title.setVisibility(View.VISIBLE);
        }
    }

}

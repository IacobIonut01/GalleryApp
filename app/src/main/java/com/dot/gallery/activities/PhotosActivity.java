package com.dot.gallery.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dot.gallery.R;
import com.dot.gallery.adapters.TodayAdapter;
import com.dot.gallery.model.MediaCard;
import com.dot.gallery.utils.GridSpacingItemDecoration;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PhotosActivity extends AppCompatActivity {

    RecyclerView recycler;
    List<MediaCard> allList = new ArrayList<>();
    TodayAdapter adapter;
    String album_track;
    TextView album_name;

    ImageButton go_back;
    FloatingActionButton refresh;

    GridLayoutManager layoutManager;

    @Override
    protected void onResume() {
        super.onResume();
        new parseMedia().execute();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences prefs = getSharedPreferences("app_theme_style", MODE_PRIVATE);
        switch (prefs.getInt("style", 0)) {
            case 0:
                setTheme(R.style.AppTheme);
                break;
            case 1:
                setTheme(R.style.DarkTheme);
                break;
        }
        setContentView(R.layout.activity_album);
        Intent intent = getIntent();
        album_track = intent.getStringExtra("album");
        recycler = findViewById(R.id.recycler);
        go_back = findViewById(R.id.go_backs);
        album_name = findViewById(R.id.album_name_title);
        album_name.setText(album_track);
        layoutManager = new GridLayoutManager(getApplicationContext(), 3);
        recycler.addItemDecoration(new GridSpacingItemDecoration(layoutManager.getSpanCount(), dp(6), true));
        refresh = findViewById(R.id.refresh);
        go_back.setOnClickListener(v -> finish());
        refresh.setOnClickListener(v -> {
            new parseMedia().execute();
        });
    }

    class parseMedia extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            allList.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            Cursor cursor;
            int column_index_data, vcolumn_index_data, column_index_album, column_index_timestamp, vcolumn_index_timestamp, vcolumn_index_thumbnail;
            String absolutePathOfImage, album, thumb, vpath;
            int timestamp, vtimestamp;
            final String orderBy = MediaStore.Images.Media.DATE_MODIFIED;

            String[] projection = {
                    MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.TITLE,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.MediaColumns.DATE_MODIFIED};
            String[] vprojection = {
                    MediaStore.MediaColumns.DATA,
                    MediaStore.Video.Media.DATE_MODIFIED,
                    MediaStore.Video.Media.TITLE,
                    MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.Video.Thumbnails.DATA};

            Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uriInternal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
            Uri vuriExternal = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
            Uri vuriInternal = MediaStore.Video.Media.INTERNAL_CONTENT_URI;

            Cursor cursorExternal = getContentResolver().query(uriExternal, projection, "bucket_display_name = \""+album_track+"\"",
                    null, orderBy + " DESC");
            Cursor cursorInternal = getContentResolver().query(uriInternal, projection, "bucket_display_name = \""+album_track+"\"",
                    null, orderBy + " DESC");
            Cursor cursorVExternal = getContentResolver().query(vuriExternal, vprojection, "bucket_display_name = \""+album_track+"\"",
                    null, MediaStore.Video.Media.DATE_MODIFIED + " DESC");
            Cursor cursorVInternal = getContentResolver().query(vuriInternal, vprojection, "bucket_display_name = \""+album_track+"\"",
                    null, MediaStore.Video.Media.DATE_MODIFIED + " DESC");
            cursor = new MergeCursor(new Cursor[]{cursorExternal,cursorInternal});
            Cursor vcursor = new MergeCursor(new Cursor[]{cursorVExternal,cursorVInternal});

            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_album = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            column_index_timestamp = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED);
            vcolumn_index_data = vcursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            vcolumn_index_timestamp = vcursor.getColumnIndexOrThrow(MediaStore.Video.Media.DATE_MODIFIED);
            vcolumn_index_thumbnail = vcursor.getColumnIndexOrThrow(MediaStore.Video.Thumbnails.DATA);
            while (cursor.moveToNext() || vcursor.moveToNext()) {
                if (cursor.getColumnCount() > 0) {
                    absolutePathOfImage = cursor.getString(column_index_data);
                    album = cursor.getString(column_index_album);
                    timestamp = cursor.getInt(column_index_timestamp);
                    File checkPath = new File(absolutePathOfImage);
                    if (checkPath.exists()) {
                        allList.add(new MediaCard(absolutePathOfImage, album, String.valueOf(timestamp)));
                    }
                }
                if (vcursor.moveToNext() && vcursor.getColumnCount() > 0) {
                    vpath = vcursor.getString(vcolumn_index_data);
                    thumb = vcursor.getString(vcolumn_index_thumbnail);
                    vtimestamp = vcursor.getInt(vcolumn_index_timestamp);
                    File vcheckPath = new File(vpath);
                    if (vcheckPath.exists()) {
                        allList.add(new MediaCard(vpath, String.valueOf(vtimestamp), thumb, true));
                    }
                }
            }
            allList.sort((o1, o2) -> timeFormat(o2.getTimestamp()).compareTo(timeFormat(o1.getTimestamp())));
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            adapter = new TodayAdapter(PhotosActivity.this, allList);
            recycler.setAdapter(adapter);
            recycler.setLayoutManager(layoutManager);
            if (allList.size() < 1) {
                finish();
            }
        }
    }

    private int dp(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private Date timeFormat(String timestamp) {
        Calendar cal = Calendar.getInstance(Locale.ENGLISH);
        cal.setTimeInMillis(Integer.valueOf(timestamp) * 1000L);
        return cal.getTime();
    }
}
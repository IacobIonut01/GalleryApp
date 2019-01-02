package com.dot.gallery.activities;

import android.content.Intent;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.animation.Animation;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dot.gallery.R;
import com.dot.gallery.adapters.TodayAdapter;
import com.dot.gallery.model.TodayCard;
import com.dot.gallery.utils.GridSpacingItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class PhotosActivity extends AppCompatActivity {

    RecyclerView recycler;
    List<TodayCard> allList = new ArrayList<>();
    TodayAdapter adapter;
    String album_track;
    TextView album_name;

    ImageButton go_back, refresh;

    GridLayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        new LoadAllAlbums().execute();
        go_back.setOnClickListener(v -> finish());
        refresh.setOnClickListener(v -> {
            new LoadAllAlbums().execute();
        });
    }

    class LoadAllAlbums extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            allList.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            Cursor cursor;
            int column_index_data, column_index_album, column_index_folder_name, column_index_timestamp;
            String absolutePathOfImage, name, album;
            int timestamp;

            String[] projection = { MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.TITLE,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.MediaColumns.DATE_MODIFIED};

            Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uriInternal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

            Cursor cursorExternal = getContentResolver().query(uriExternal, projection, "bucket_display_name = \""+album_track+"\"",
                    null, null);
            Cursor cursorInternal = getContentResolver().query(uriInternal, projection, "bucket_display_name = \""+album_track+"\"",
                    null, null);
            cursor = new MergeCursor(new Cursor[]{cursorExternal,cursorInternal});


            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_folder_name = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.TITLE);
            column_index_album = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            column_index_timestamp = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);
                name = cursor.getString(column_index_folder_name);
                album = cursor.getString(column_index_album);
                timestamp = cursor.getInt(column_index_timestamp);
                File checkPath = new File(absolutePathOfImage);
                if (checkPath.exists())
                allList.add(new TodayCard(absolutePathOfImage, name, album, String.valueOf(timestamp)));
            }
            allList.sort((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            adapter = new TodayAdapter(PhotosActivity.this, allList);
            recycler.setAdapter(adapter);
            recycler.setLayoutManager(layoutManager);
        }
    }

    private int dp(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}

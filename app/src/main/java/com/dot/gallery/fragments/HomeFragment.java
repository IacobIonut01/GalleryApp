package com.dot.gallery.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dot.gallery.R;
import com.dot.gallery.adapters.TodayAdapter;
import com.dot.gallery.adapters.AlbumAdapter;
import com.dot.gallery.model.MediaCard;
import com.dot.gallery.model.TodayCard;
import com.dot.gallery.model.AlbumCard;
import com.dot.gallery.model.VideoCard;
import com.dot.gallery.utils.GridSpacingItemDecoration;
import com.dot.gallery.utils.MediaParser;
import com.dot.gallery.utils.VerticalSpaceItemDecoration;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;

public class HomeFragment extends Fragment {

    private RecyclerView todayRecycler, allRecycler;
    private List<MediaCard> todayList = new ArrayList<>();
    private List<AlbumCard> allList = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private RelativeLayout grant_sd;
    private MaterialCardView c_grant_sd;
    private SharedPreferences pref;
    private LinearLayout no_media;
    private TextView title_albums;
    private int columns = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        todayRecycler = view.findViewById(R.id.today_recycler);
        allRecycler = view.findViewById(R.id.all_recycler);
        no_media = view.findViewById(R.id.no_media_found);
        title_albums = view.findViewById(R.id.title_albums);
        allRecycler.setHasFixedSize(true);
        pref = getActivity().getSharedPreferences("sdcard_perm", MODE_PRIVATE);
        grant_sd = view.findViewById(R.id.grant_sdcard);
        c_grant_sd = view.findViewById(R.id.grant_sdcard_c);
        c_grant_sd.setVisibility(pref.getBoolean("result", false) ? View.GONE : View.VISIBLE);
        grant_sd.setOnClickListener(v -> {
            if (!pref.getBoolean("result", false)) {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT_TREE);
                intent.putExtra("android.content.extra.SHOW_ADVANCED", true);
                startActivityForResult(intent, 1);
            }
        });
        allRecycler.addItemDecoration(new GridSpacingItemDecoration(columns, dp(6), false));
        todayRecycler.addItemDecoration(new VerticalSpaceItemDecoration(dp(12)));
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(() -> {
            //loadImages();
            new LoadTodayList().execute();
            new LoadAllAlbums().execute();
        });
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            //loadImages();
            new LoadTodayList().execute();
            new LoadAllAlbums().execute();
        });
        loadSearchEngine(view);
        return view;
    }

    /*public void loadImages() {
        TextView f = getView().findViewById(R.id.title_fav);
        favs.clear();
        SharedPreferences prefs = getActivity().getSharedPreferences("favourite_images", MODE_PRIVATE);
        String httpParamJSONList = prefs.getString("favourite_images", "");
        selectedList = new Gson().fromJson(httpParamJSONList, new TypeToken<List<FavouriteCard>>() {
        }.getType());
        if (selectedList == null) {
            favRecycler.setVisibility(View.GONE);
            f.setVisibility(View.GONE);
        } else {
            favs.addAll(selectedList);
            favRecycler.setVisibility(View.VISIBLE);
            f.setVisibility(View.VISIBLE);
            Favadapter = new FavouriteAdapter(getActivity(), favs);
            favRecycler.setAdapter(Favadapter);
            favRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        }
    }*/

    public void loadSearchEngine(View view) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        boolean finish = true;
        boolean selectionWasCorrect = false;
        if (resultCode == RESULT_OK) {
            Uri treeUri = data.getData();
            int takeFlags = data.getFlags() & (Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            getActivity().getContentResolver().takePersistableUriPermission(treeUri, takeFlags);
            SharedPreferences.Editor editor = pref.edit();
            editor.putBoolean("result", true);
            editor.apply();
            c_grant_sd.setVisibility(pref.getBoolean("result", false) ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        switch (newConfig.orientation) {
            case Configuration.ORIENTATION_PORTRAIT:
                columns = 2;
                break;
            case Configuration.ORIENTATION_LANDSCAPE:
                columns = 3;
                break;
        }
    }

    private int dp(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private String getAlbumPath(String fullPath) {
        return fullPath.replace(fullPath.substring(fullPath.lastIndexOf("/")), "");
    }

    private String getCount(String album_name) {
        Uri uriExternal = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        Uri uriInternal = android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        Uri vuriExternal = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        Uri vuriInternal = MediaStore.Video.Media.INTERNAL_CONTENT_URI;

        String[] projection = {MediaStore.MediaColumns.DATA,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME, MediaStore.MediaColumns.DATE_MODIFIED};
        String[] vprojection = {
                MediaStore.MediaColumns.DATA,
                MediaStore.MediaColumns.DATE_MODIFIED,
                MediaStore.Video.Media.TITLE,
                MediaStore.Video.Media.BUCKET_DISPLAY_NAME,
                MediaStore.Video.Thumbnails.DATA};
        Cursor cursorExternal = getContext().getContentResolver().query(uriExternal, projection, "bucket_display_name = \"" + album_name + "\"", null, null);
        Cursor cursorInternal = getContext().getContentResolver().query(uriInternal, projection, "bucket_display_name = \"" + album_name + "\"", null, null);
        Cursor cursorVExternal = getContext().getContentResolver().query(vuriExternal, vprojection, "bucket_display_name = \"" + album_name + "\"", null, null);
        Cursor cursorVInternal = getContext().getContentResolver().query(vuriInternal, vprojection, "bucket_display_name = \"" + album_name + "\"", null, null);
        Cursor cursor = new MergeCursor(new Cursor[]{cursorExternal, cursorInternal, cursorVExternal, cursorVInternal});

        return String.valueOf(cursor.getCount());
    }

    private class LoadTodayList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            todayList.clear();
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

            Cursor cursorExternal = getActivity().getContentResolver().query(uriExternal, projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, orderBy + " DESC");
            Cursor cursorInternal = getActivity().getContentResolver().query(uriInternal, projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, orderBy + " DESC");
            Cursor cursorVExternal = getActivity().getContentResolver().query(vuriExternal, vprojection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, orderBy + " DESC");
            Cursor cursorVInternal = getActivity().getContentResolver().query(vuriInternal, vprojection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
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
                    todayList.add(new TodayCard(absolutePathOfImage, album, String.valueOf(timestamp)));
                }
                if (vcursor.moveToNext()) {
                    vpath = vcursor.getString(vcolumn_index_data);
                    thumb = vcursor.getString(vcolumn_index_thumbnail);
                    vtimestamp = vcursor.getInt(vcolumn_index_timestamp);
                    File vcheckPath = new File(vpath);
                    if (currentStamp >= vtimestamp && vtimestamp >= yesterdayStamp && vcheckPath.exists()) {
                        todayList.add(new VideoCard(vpath, String.valueOf(vtimestamp), thumb));
                    }
                }
            }
            todayList.sort((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            TodayAdapter adapter = new TodayAdapter(getActivity(), todayList);
            todayRecycler.setAdapter(adapter);
            todayRecycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
            TextView tx = getView().findViewById(R.id.title_recents);
            if (todayList.size() == 0)
                tx.setVisibility(View.GONE);
            else
                tx.setVisibility(View.VISIBLE);
        }
    }

    private class LoadAllAlbums extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            allList.clear();
        }

        protected String doInBackground(String... args) {
            String xml = "";

            Cursor cursor;
            int column_index_data, column_index_album, column_index_timestamp;
            String absolutePathOfImage, album;
            int timestamp;
            final String orderBy = MediaStore.Images.Media.DATE_TAKEN;

            String[] projection = {
                    MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.TITLE,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.MediaColumns.DATE_MODIFIED};

            Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uriInternal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;

            Cursor cursorExternal = getActivity().getContentResolver().query(uriExternal, projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, orderBy + " DESC");
            Cursor cursorInternal = getActivity().getContentResolver().query(uriInternal, projection, "_data IS NOT NULL) GROUP BY (bucket_display_name",
                    null, orderBy + " DESC");
            cursor = new MergeCursor(new Cursor[]{cursorExternal, cursorInternal});

            column_index_data = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            column_index_album = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME);
            column_index_timestamp = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATE_MODIFIED);
            while (cursor.moveToNext()) {
                absolutePathOfImage = cursor.getString(column_index_data);
                album = cursor.getString(column_index_album);
                timestamp = cursor.getInt(column_index_timestamp);
                File checkPath = new File(absolutePathOfImage);
                if (checkPath.exists())
                    allList.add(new AlbumCard(absolutePathOfImage, album, getCount(album), String.valueOf(timestamp)));
            }
            allList.sort((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
            LinkedHashSet<AlbumCard> listToSet = new LinkedHashSet<>(allList);
            allList.clear();
            allList.addAll(listToSet);
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            AlbumAdapter adapter = new AlbumAdapter(getActivity(), allList);
            allRecycler.setAdapter(adapter);
            allRecycler.setLayoutManager(new GridLayoutManager(getContext(), columns));
            swipeRefreshLayout.setRefreshing(false);

            if (allList.size() < 1) {
                title_albums.setVisibility(View.GONE);
                no_media.setVisibility(View.VISIBLE);
            } else {
                title_albums.setVisibility(View.VISIBLE);
                no_media.setVisibility(View.GONE);
            }
        }
    }

}

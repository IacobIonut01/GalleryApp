package com.dot.gallery.fragments;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MergeCursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import com.dot.gallery.R;
import com.dot.gallery.adapters.PickerAdapter;
import com.dot.gallery.model.FavouriteCard;
import com.dot.gallery.model.PickerCard;
import com.dot.gallery.utils.GridSpacingItemDecoration;
import com.dot.gallery.utils.RoundedSheetFragment;
import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.Context.MODE_PRIVATE;

public class PickerSheet extends RoundedSheetFragment {

    private List<PickerCard> phts = new ArrayList<>();
    private List<FavouriteCard> selected = new ArrayList<>();
    private RecyclerView recyclerView;
    private ProgressBar prg;
    private Button picker;
    private SharedPreferences shared;

    @Override
    public void dismiss() {
        super.dismiss();
        FavouriteFragment activity = (FavouriteFragment) getActivity().getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.fragment_pager + ":" + 1);
        activity.loadImages();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view =  inflater.inflate(R.layout.sheet_picker, container, false);
        recyclerView = view.findViewById(R.id.all_photos);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dp(6), false));
        prg = view.findViewById(R.id.loader);
        shared = getActivity().getSharedPreferences("favourite_images", MODE_PRIVATE);
        new LoadTodayList().execute();

        picker = view.findViewById(R.id.pick);
        picker.setEnabled(false);
        picker.setOnClickListener(v -> {
            updateList();
            dismiss();
        });
        return view;
    }

    private int dp(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    private class LoadTodayList extends AsyncTask<String, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            phts.clear();
            prg.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(String... args) {
            String xml = "";

            Cursor cursor;
            int column_index_data, column_index_folder_name, column_index_album, column_index_timestamp;
            String absolutePathOfImage, name, album;
            int timestamp;

            String[] projection = {MediaStore.MediaColumns.DATA,
                    MediaStore.Images.Media.TITLE,
                    MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
                    MediaStore.MediaColumns.DATE_MODIFIED};
            Uri uriExternal = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
            Uri uriInternal = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
            Cursor cursorExternal = getActivity().getContentResolver().query(uriExternal, projection, "_data IS NOT NULL",
                    null, null);
            Cursor cursorInternal = getActivity().getContentResolver().query(uriInternal, projection, "_data IS NOT NULL",
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
                if (checkPath.exists()) {
                    String finalAbsolutePathOfImage = absolutePathOfImage;
                    int finalTimestamp = timestamp;
                    phts.add(new PickerCard(absolutePathOfImage, name, album, String.valueOf(timestamp), v -> {
                        FavouriteCard c = new FavouriteCard();
                        c.setPath(finalAbsolutePathOfImage);
                        c.setTimestamp(String.valueOf(finalTimestamp));
                        selected.add(c);
                        if (selected.size() == 1)
                            picker.setEnabled(true);
                    }));
                }
            }
            phts.sort((o1, o2) -> o2.getTimestamp().compareTo(o1.getTimestamp()));
            return xml;
        }

        @Override
        protected void onPostExecute(String xml) {
            prg.setVisibility(View.GONE);
            PickerAdapter adapter = new PickerAdapter(getActivity(), phts);
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        }
    }

    private void updateList() {
        String selectedOutput = new Gson().toJson(selected);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("favourite_images", selectedOutput);
        editor.apply();
    }
}

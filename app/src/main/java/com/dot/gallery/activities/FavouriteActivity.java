package com.dot.gallery.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.ImageButton;

import com.dot.gallery.R;
import com.dot.gallery.adapters.FavouriteAdapter;
import com.dot.gallery.fragments.HomeFragment;
import com.dot.gallery.fragments.PickerSheet;
import com.dot.gallery.model.FavouriteCard;
import com.dot.gallery.utils.GridSpacingItemDecoration;
import com.dot.gallery.utils.TinyDB;
import com.dot.gallery.utils.VerticalSpaceItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

public class FavouriteActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    List<FavouriteCard> favs = new ArrayList<>();
    FavouriteAdapter adapter;
    List<FavouriteCard> selectedList;
    public static SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);
        ImageButton go_back = findViewById(R.id.go_backs);
        go_back.setOnClickListener(v -> finish());
        recyclerView = findViewById(R.id.add_fav);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dp(6), false));
        swipeRefreshLayout = findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(this::loadImages);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            loadImages();
        });
    }

    public void loadImages() {
        favs.clear();
        favs.add(new FavouriteCard(true));
        SharedPreferences prefs = getSharedPreferences("favourite_images", MODE_PRIVATE);
        String httpParamJSONList = prefs.getString("favourite_images", "");
        selectedList = new Gson().fromJson(httpParamJSONList, new TypeToken<List<FavouriteCard>>() {}.getType());
        if (selectedList != null) {
            favs.addAll(selectedList);
        }
        adapter = new FavouriteAdapter(this, favs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        swipeRefreshLayout.setRefreshing(false);
    }

    private int dp(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}

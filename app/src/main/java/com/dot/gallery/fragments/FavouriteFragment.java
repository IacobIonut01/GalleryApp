package com.dot.gallery.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dot.gallery.R;
import com.dot.gallery.adapters.FavouriteAdapter;
import com.dot.gallery.model.FavouriteCard;
import com.dot.gallery.utils.GridSpacingItemDecoration;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static android.content.Context.MODE_PRIVATE;


public class FavouriteFragment extends Fragment {

    RecyclerView recyclerView;
    List<FavouriteCard> favs = new ArrayList<>();
    FavouriteAdapter adapter;
    List<FavouriteCard> selectedList;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourites, container, false);
        recyclerView = view.findViewById(R.id.add_fav);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dp(6), false));
        swipeRefreshLayout = view.findViewById(R.id.swipeToRefresh);
        swipeRefreshLayout.setOnRefreshListener(this::loadImages);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        swipeRefreshLayout.post(() -> {
            swipeRefreshLayout.setRefreshing(true);
            loadImages();
        });
        return view;
    }

    public void loadImages() {
        favs.clear();
        favs.add(new FavouriteCard(true));
        SharedPreferences prefs = getActivity().getSharedPreferences("favourite_images", MODE_PRIVATE);
        String httpParamJSONList = prefs.getString("favourite_images", "");
        selectedList = new Gson().fromJson(httpParamJSONList, new TypeToken<List<FavouriteCard>>() {}.getType());
        if (selectedList != null) {
            favs.addAll(selectedList);
        }
        adapter = new FavouriteAdapter(getActivity(), favs);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        swipeRefreshLayout.setRefreshing(false);
    }

    private int dp(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}

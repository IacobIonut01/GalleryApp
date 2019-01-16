package com.dot.gallery.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dot.gallery.R;
import com.dot.gallery.activities.FavouriteActivity;
import com.dot.gallery.model.FavouriteCard;
import com.dot.gallery.utils.RoundedSheetFragment;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import static android.content.Context.MODE_PRIVATE;

public class FavDeleteSheet extends RoundedSheetFragment {

    List<FavouriteCard> paths = new ArrayList<>();
    FavouriteCard fav;
    MaterialButton delete;
    private AppCompatActivity mActivity;
    private FragmentActivity fActivity;
    private SharedPreferences shared;

    @Override
    public void dismiss() {
        if (mActivity != null) {
            FavouriteActivity activity = (FavouriteActivity) mActivity;
            activity.loadImages();
        } else {
            HomeFragment frg = (HomeFragment) fActivity.getSupportFragmentManager().findFragmentByTag("main");
            frg.loadImages();
            frg.getFavAdapter().notifyDataSetChanged();
        }
        super.dismiss();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view =  inflater.inflate(R.layout.sheet_remove_fav, container, false);
        delete = view.findViewById(R.id.delete);
        shared = getActivity().getSharedPreferences("favourite_images", MODE_PRIVATE);
        SharedPreferences prefs = getActivity().getSharedPreferences("favourite_images", MODE_PRIVATE);
        String httpParamJSONList = prefs.getString("favourite_images", "");
        paths = new Gson().fromJson(httpParamJSONList, new TypeToken<List<FavouriteCard>>() {}.getType());
        initSheet();
        return view;
    }

    private void initSheet() {
        delete.setOnClickListener(v -> {
            if (mActivity != null) {
                paths.remove(0);
                paths.remove(fav);
            } else {
                paths.remove(fav);
            }
            updateList();
            dismiss();
        });
    }

    public void setFavouriteCard(FavouriteCard fav) {
        this.fav = fav;
    }

    public void setmActivity(AppCompatActivity mActivity) {
        this.mActivity = mActivity;
    }

    public void setfActivity(FragmentActivity fActivity) {
        this.fActivity = fActivity;
    }

    private void updateList() {
        String selectedOutput = new Gson().toJson(paths);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("favourite_images", selectedOutput);
        editor.apply();
    }
}

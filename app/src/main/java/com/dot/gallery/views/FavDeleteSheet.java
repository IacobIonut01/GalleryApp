package com.dot.gallery.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dot.gallery.R;
import com.dot.gallery.fragments.FavouriteFragment;
import com.dot.gallery.fragments.RoundedSheetFragment;
import com.dot.gallery.model.FavouriteCard;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import static android.content.Context.MODE_PRIVATE;

public class FavDeleteSheet extends RoundedSheetFragment {

    private List<FavouriteCard> paths = new ArrayList<>();
    private int pos;
    private MaterialButton delete;
    private FragmentActivity mActivity;
    private SharedPreferences shared;

    @Override
    public void dismiss() {
        FavouriteFragment activity = (FavouriteFragment) mActivity.getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.fragment_pager + ":" + 1);
        activity.loadImages();
        super.dismiss();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.sheet_remove_fav, container, false);
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
            paths.remove(pos-1);
            updateList();
            dismiss();
        });
    }

    public void setPosition(int pos) {
        this.pos = pos;
    }

    public void setmActivity(FragmentActivity mActivity) {
        this.mActivity = mActivity;
    }

    private void updateList() {
        String selectedOutput = new Gson().toJson(paths);
        SharedPreferences.Editor editor = shared.edit();
        editor.putString("favourite_images", selectedOutput);
        editor.apply();
    }
}

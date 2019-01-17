package com.dot.gallery.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.dot.gallery.R;
import com.dot.gallery.model.FavouriteCard;
import com.dot.gallery.utils.RoundedSheetFragment;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import static android.content.Context.MODE_PRIVATE;

public class SelectThemeSheet extends RoundedSheetFragment {

    private ImageButton close;
    private LinearLayout selWhite, selDark;
    private int selected = -1;
    private SharedPreferences shared;

    @Override
    public void dismiss() {
        super.dismiss();
        if (selected != -1)
        writeThemeStyle();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.choose_style, container, false);
        close = view.findViewById(R.id.close_style);
        selWhite = view.findViewById(R.id.select_white);
        selDark = view.findViewById(R.id.select_dark);
        selWhite.setOnClickListener(v -> selected = 0);
        selDark.setOnClickListener(v -> selected = 1);
        close.setOnClickListener(v -> dismiss());
        shared = getActivity().getSharedPreferences("app_theme_style", MODE_PRIVATE);
        return view;
    }


    private void writeThemeStyle() {
        SharedPreferences.Editor editor = shared.edit();
        editor.putInt("style", selected);
        editor.apply();
        Intent i = getActivity().getBaseContext().getPackageManager().getLaunchIntentForPackage(getActivity().getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
    }
}

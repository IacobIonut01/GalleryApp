package com.dot.gallery.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.dot.gallery.R;
import com.dot.gallery.fragments.RoundedSheetFragment;
import com.google.android.material.button.MaterialButton;

import androidx.annotation.NonNull;

import static android.content.Context.MODE_PRIVATE;

public class SelectThemeSheet extends RoundedSheetFragment {

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
        MaterialButton close = view.findViewById(R.id.close_style);
        LinearLayout selWhite = view.findViewById(R.id.select_white);
        LinearLayout selDark = view.findViewById(R.id.select_dark);
        ImageView selectedWhite = view.findViewById(R.id.selectedWhite);
        ImageView selectedDark = view.findViewById(R.id.selectedDark);
        selWhite.setOnClickListener(v -> {
            selectedWhite.setVisibility(View.VISIBLE);
            selectedDark.setVisibility(View.GONE);
            close.setText("Apply");
            selected = 0;
        });
        selDark.setOnClickListener(v -> {
            selectedWhite.setVisibility(View.GONE);
            selectedDark.setVisibility(View.VISIBLE);
            close.setText("Apply");
            selected = 1;
        });
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

package com.dot.gallery.activities;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.dot.gallery.R;
import com.dot.gallery.fragments.SelectThemeSheet;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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
        setContentView(R.layout.activity_settings);
        ImageButton go_back = findViewById(R.id.go_backs);
        go_back.setOnClickListener(v -> finish());
        RelativeLayout styler = findViewById(R.id.open_styler);
        styler.setOnClickListener(v -> {
            SelectThemeSheet selectThemeSheet = new SelectThemeSheet();
            selectThemeSheet.show(getSupportFragmentManager(), "styler");
        });
    }
}

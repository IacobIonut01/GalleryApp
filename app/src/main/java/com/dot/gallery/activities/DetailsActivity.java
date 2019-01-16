package com.dot.gallery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageButton;

import com.dot.gallery.R;
import com.dot.gallery.views.DetailsSheetView;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        getWindow().setNavigationBarColor(getColor(android.R.color.black));
        DetailsSheetView sheetView = findViewById(R.id.detailsSheet);
        sheetView.setContainerActivity(this);
        Intent intent = getIntent();
        List<String> path = intent.getStringArrayListExtra("paths");
        int pos = intent.getIntExtra("pos", 0);
        sheetView.setNames(intent.getStringArrayListExtra("names"));
        sheetView.setImage(path);
        sheetView.setPosition(pos);
        ImageButton go_back = findViewById(R.id.go_backs);
        go_back.setOnClickListener(v -> finish());
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        // Hide the nav bar and status bar
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    private void showSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
    }

    public boolean isFullScreen() {
        int flg = getWindow().getAttributes().flags;
        boolean flag = false;
        if ((flg & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
            flag = true;
        }
        return flag;
    }
}

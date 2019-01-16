package com.dot.gallery.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import com.dot.gallery.R;
import com.dot.gallery.fragments.HomeFragment;
import com.dot.gallery.views.BottomSheetView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //BottomSheetView sheetView = findViewById(R.id.root_bt_sheet);
        //sheetView.setFragment(getSupportFragmentManager(), new HomeFragment());
        getSupportFragmentManager().beginTransaction().replace(R.id.frgcnt, new HomeFragment(), "main").commitNow();
    }



}

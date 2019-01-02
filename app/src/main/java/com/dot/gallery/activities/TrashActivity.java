package com.dot.gallery.activities;

import android.os.Bundle;
import android.widget.ImageButton;

import com.dot.gallery.R;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class TrashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash_can);
        ImageButton go_back = findViewById(R.id.go_backs);
        go_back.setOnClickListener(v -> finish());
    }
}

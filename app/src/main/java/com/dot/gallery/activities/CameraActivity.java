package com.dot.gallery.activities;

import android.os.Bundle;

import com.camerakit.CameraKitView;
import com.dot.gallery.R;

import androidx.appcompat.app.AppCompatActivity;

public class CameraActivity extends AppCompatActivity {

    CameraKitView cameraKitView;

    @Override
    public void onCreate(Bundle onSavedInstance) {
        super.onCreate(onSavedInstance);
        setContentView(R.layout.camera_layout);
        cameraKitView = findViewById(R.id.camera);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraKitView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraKitView.onResume();
    }

    @Override
    protected void onPause() {
        cameraKitView.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        cameraKitView.onStop();
        super.onStop();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        cameraKitView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}

package com.dot.gallery.activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.MediaController;
import android.widget.VideoView;

import com.dot.gallery.R;

import androidx.appcompat.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    VideoView videoView;

    @Override
    public void onCreate(Bundle onSavedInstance) {
        super.onCreate(onSavedInstance);
        setContentView(R.layout.activity_video);
        videoView = findViewById(R.id.videoView);
        MediaController mediaController= new MediaController(this);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        Intent intent = getIntent();
        videoView.setVideoPath(intent.getStringExtra("video_path"));
        videoView.requestFocus();
        videoView.start();
    }
}

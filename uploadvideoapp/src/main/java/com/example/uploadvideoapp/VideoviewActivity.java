package com.example.uploadvideoapp;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static android.os.Environment.DIRECTORY_DCIM;


public class VideoviewActivity extends AppCompatActivity {

    android.widget.VideoView videoView;
    MediaController mMediaController;
    String TAG = "videoView";
    FileInputStream in;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        Intent intent = getIntent();
        String filename = intent.getStringExtra("Filename");
        Log.d(TAG, "Filenname : "+filename);

        try {
            FileInputStream in = openFileInput(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        if(in != null) {
            videoView = (android.widget.VideoView) findViewById(R.id.videoView);
            mMediaController = new MediaController(this);
            mMediaController.setMediaPlayer(videoView);
            videoView.setVideoPath(DIRECTORY_DCIM+"/"+filename);
            videoView.requestFocus();
            videoView.setMediaController(mMediaController);
            videoView.start();
        }
    }




}

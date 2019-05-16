package com.example.uploadvideoapp;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;


public class Video_View extends AppCompatActivity {

    android.widget.VideoView videoView;
    MediaController mMediaController;
    String TAG = "videoView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_view);

        Intent intent = getIntent();
        String videoUri = intent.getStringExtra("VideoUri");
        Log.d(TAG, "videoUri : "+videoUri);

        videoView =(android.widget.VideoView) findViewById(R.id.videoView);
        mMediaController = new MediaController(this);
        mMediaController.setMediaPlayer(videoView);
        videoView.setVideoURI(Uri.parse(videoUri));
        videoView.requestFocus();
        videoView.setMediaController(mMediaController);
        videoView.start();




    }
}

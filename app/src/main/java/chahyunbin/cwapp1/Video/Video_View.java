package chahyunbin.cwapp1.Video;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.MediaController;
import android.widget.VideoView;

import java.io.IOException;

import chahyunbin.cwapp1.R;

public class Video_View extends Activity  {


    VideoView videoView;
    MediaController mMediaController;
    String TAG = "videoView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Intent intent = getIntent();
        String videoUri = intent.getStringExtra("VideoUri");
        Log.d(TAG, "videoUri : " + videoUri);

        videoView = (VideoView) findViewById(R.id.videoView);
        mMediaController = new MediaController(this);

        mMediaController.setMediaPlayer(videoView);
        videoView.setVideoURI(Uri.parse(videoUri));
        videoView.requestFocus();
        videoView.setMediaController(mMediaController);

        videoView.start();

    }
}

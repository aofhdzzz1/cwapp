package chahyunbin.cwapp1.Video;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.FrameLayout;
import android.widget.MediaController;

import java.io.IOException;

import chahyunbin.cwapp1.R;

public class Video_View extends Activity  {


    android.widget.VideoView videoView;
    MediaController mMediaController;
    String TAG = "videoView";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        Intent intent = getIntent();
        String videoUri = intent.getStringExtra("VideoUri");
        Log.d(TAG, "videoUri : " + videoUri);

        videoView = (android.widget.VideoView) findViewById(R.id.videoView);

        mMediaController.setMediaPlayer(videoView);
        videoView.setVideoURI(Uri.parse(videoUri));
        videoView.requestFocus();
        videoView.setMediaController(mMediaController);

        videoView.start();

    }
}

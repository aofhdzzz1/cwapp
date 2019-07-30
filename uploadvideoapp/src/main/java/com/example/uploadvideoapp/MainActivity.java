package com.example.uploadvideoapp;

import android.os.Bundle;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    //fragment
    private VideoFragment videoFragment = new VideoFragment();
    private VideoListFragment videoListFragment = new VideoListFragment();
    private ImageFragment imageFragment = new ImageFragment();
    private FragmentManager fragmentManager = getSupportFragmentManager();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment selectedFragment = null;
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_video:
//                    mTextMessage.setText(R.string.title_video);
//                    selectedFragment = VideoFragment.newInstance();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.container,selectedFragment).commit();
                    transaction.replace(R.id.frame_layout,videoFragment).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_image:
//                    mTextMessage.setText(R.string.title_image);
                    transaction.replace(R.id.frame_layout,imageFragment).commitAllowingStateLoss();
                    return true;
                case R.id.navigation_videolist:
//                    mTextMessage.setText(R.string.title_videolist);
//                    selectedFragment = VideoListFragment.newInstance();
//                    getSupportFragmentManager().beginTransaction().replace(R.id.container,selectedFragment).commit();
                    transaction.replace(R.id.frame_layout,videoListFragment).commitAllowingStateLoss();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(android.R.style.Theme_Light_NoTitleBar_Fullscreen);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //Manually displaying the first fragment - one time only
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.frame_layout, videoFragment).commitAllowingStateLoss();
    }

}

package com.example.chahyunbin.cwapp;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chahyunbin.cwapp.AdminMember.AdminMember;
import com.example.chahyunbin.cwapp.Bible.Bible;
import com.example.chahyunbin.cwapp.Login.FirebaseUI;
import com.example.chahyunbin.cwapp.Login.GoogleSignInLogin;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends Activity {

    Button button1,button2,button3;
    ImageView imageView;
    String username;
    String phonenumber;
 GoogleSignInLogin googlelogin;
   // FirebaseUI firebaseUI;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    public static boolean outboolean =false;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);




        TextView textView = (TextView)findViewById(R.id.loginName);
        textView.setText(googlelogin.googleName);



        //textView.setText(firebaseUI.userName);

        findViewById(R.id.logoutbtn).setOnClickListener(myClick);
        findViewById(R.id.button1).setOnClickListener(myClick);
        findViewById(R.id.button2).setOnClickListener(myClick);
        findViewById(R.id.button3).setOnClickListener(myClick);
    }

        Button.OnClickListener myClick = new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.button1:
                        startActivity(new Intent(MainActivity.this,AddMember.class));
                        break;
                    case R.id.button2:
                        startActivity(new Intent(MainActivity.this,AdminMember.class));
                        break;
                    case R.id.button3:
                        startActivity(new Intent(MainActivity.this, Bible.class));
                        break;
                    case R.id.logoutbtn:
                        googlelogin.signOut();
                        //AuthUI.getInstance().signOut(getApplicationContext());
                        finish();
                        startActivity(new Intent(MainActivity.this,GoogleSignInLogin.class));

                }
            }
        };

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;

        if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime)
        {
            super.onBackPressed();
        }
        else
        {
            backPressedTime = tempTime;
            Toast.makeText(getApplicationContext(), "한번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
            outboolean = true;
        }


    }
}

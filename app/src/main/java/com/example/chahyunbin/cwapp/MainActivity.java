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

import com.example.chahyunbin.cwapp.AdminMember.AdminMember;
import com.example.chahyunbin.cwapp.Bible.Bible;
import com.example.chahyunbin.cwapp.Login.Loginmenu;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends Activity {

    Button button1,button2,button3;
    ImageView imageView;

    Loginmenu loginmenu;

    String username;
    String phonenumber;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);



        String email = loginmenu.userName;
        int idx = email.indexOf("@");
        if(email != null)
           username = email.substring(0,idx);
        else
        phonenumber = loginmenu.phoneNumber.substring(5,9);



        Button logoutBtn = (Button)findViewById(R.id.logoutbtn);
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            FirebaseAuth.getInstance().signOut();
            finish();
            startActivity(new Intent(MainActivity.this,Loginmenu.class));



            }
        });
        TextView textView = (TextView)findViewById(R.id.loginName);
            if(username != null)
            textView.setText(username +"님");
            else
                textView.setText(phonenumber+"님");



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
                }
            }
        };




}

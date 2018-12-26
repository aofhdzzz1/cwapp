package chahyunbin.cwapp1.MainActivity;

import android.app.Activity;
import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import chahyunbin.cwapp1.Bible.Bible;
import chahyunbin.cwapp1.BottomBar.Tabbar_activity;
import chahyunbin.cwapp1.FirebaseDatabase_Input;
import chahyunbin.cwapp1.Login.EmailSignIn;
import chahyunbin.cwapp1.Login.LoginHome;


import chahyunbin.cwapp1.R;
import chahyunbin.cwapp1.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LeaderMainActivity extends Activity {

    Button button1,button2,button3;
    ImageView imageView;
    public static String username = null;
    String phonenumber;
    String emaildata;
    LoginHome googlelogin;
    EmailSignIn signIn;
   // FirebaseUI firebaseUI;
   TextView textView;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    public static boolean outboolean =false;

    public static String email;
    FirebaseUser user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);



        user = FirebaseAuth.getInstance().getCurrentUser();
        textView = (TextView)findViewById(R.id.loginName);


        //get email
        emaildata = user.getEmail();
        if (emaildata != null) {
            email = emaildata.substring(0, emaildata.indexOf("@"));

        }

        if(email == null){
            Intent intent = new Intent(LeaderMainActivity.this, LoginHome.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            FirebaseAuth.getInstance().signOut();
            startActivity(intent);

        }
        // find username in firebase UserInfo
        //if username is null go back to personal_Info.class

        GetUserName();








        //textView.setText(firebaseUI.userName);

        findViewById(R.id.logoutbtn).setOnClickListener(myClick);
        findViewById(R.id.button1).setOnClickListener(myClick);
        findViewById(R.id.button2).setOnClickListener(myClick);
        findViewById(R.id.button3).setOnClickListener(myClick);
    }

    private void GetUserName() {
        Query query =  FirebaseDatabase.getInstance().getReference("User/"+email).orderByChild("UserInfo");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    username = user.getName();
                    Log.d("Firebase", "username1 : "+username);
                    textView.setText(username+"님");

                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        if (username == null) {
//            Intent intent = new Intent(LeaderMainActivity.this, Personal_Info.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            startActivity(intent);
//        }
    }

    Button.OnClickListener myClick = new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch(v.getId()){
                    case R.id.button1:
                        startActivity(new Intent(LeaderMainActivity.this,FirebaseDatabase_Input.class));

                        break;
                    case R.id.button2:
                        startActivity(new Intent(LeaderMainActivity.this,Tabbar_activity.class));
                        break;
                    case R.id.button3:
                        startActivity(new Intent(LeaderMainActivity.this, Bible.class));
                        break;
                    case R.id.logoutbtn:
                        googlelogin.signOut();
                        email = null;
                        //AuthUI.getInstance().signOut(getApplicationContext());
                        finish();
                        startActivity(new Intent(LeaderMainActivity.this,LoginHome.class));

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

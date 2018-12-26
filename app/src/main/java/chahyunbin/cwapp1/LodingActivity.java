package chahyunbin.cwapp1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Timer;
import java.util.TimerTask;

import chahyunbin.cwapp1.Login.LoginHome;
import chahyunbin.cwapp1.MainActivity.LeaderMainActivity;
import chahyunbin.cwapp1.MainActivity.MemberMainActivity;
import chahyunbin.cwapp1.model.User;


public class LodingActivity extends Activity {
    private String TAG = "LodingActivity";
    ImageView imageView1, imageView2, imageView3;
    LoginHome loginHome;
    public String username ;
    public String imLeader ;
    public static FirebaseAuth mAuth;

    Handler han;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loding);
        mAuth = FirebaseAuth.getInstance();

        imageView1 = (ImageView) findViewById(R.id.image1);
        imageView2 = (ImageView) findViewById(R.id.image2);
        imageView3 = (ImageView) findViewById(R.id.image3);


        Animation anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.to_down_translate);
        Animation anim2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.to_right_translate);
        Animation anim3 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.to_left_translate);

        imageView1.startAnimation(anim);
        imageView2.startAnimation(anim2);
        imageView3.startAnimation(anim3);



       
        FirebaseUser user = mAuth.getCurrentUser();
        String email =user.getEmail();
        String emailId = email.substring(0,email.indexOf("@"));
        Query query =  FirebaseDatabase.getInstance().getReference("User/"+emailId).orderByChild("UserInfo");
        query.addValueEventListener(new ValueEventListener() {

            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user=null;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    user= snapshot.getValue(User.class);


                }
                username = user.getName();
                imLeader = user.getLeader();
                Log.d("login", "LoginHome username: "+username);
                if (username == null) {
                    Intent intent = new Intent(LodingActivity.this, Personal_Info.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    if (imLeader.equals("Leader")) {
                        Intent intent = new Intent(LodingActivity.this, LeaderMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    } else if (imLeader.equals("CellMember")) {
                        Intent intent = new Intent(LodingActivity.this, MemberMainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });











    }



}




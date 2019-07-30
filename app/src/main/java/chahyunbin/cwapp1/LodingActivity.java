package chahyunbin.cwapp1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import chahyunbin.cwapp1.Login.LoginHomeActivity;
import chahyunbin.cwapp1.MainActivity.LeaderMainActivity;
import chahyunbin.cwapp1.MainActivity.MemberMainActivity;
import chahyunbin.cwapp1.model.User;


public class LodingActivity extends Activity {
    private String TAG = "LodingActivity";
    ImageView imageView1, imageView2, imageView3;
    LoginHomeActivity loginHome;
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

               User user =null;
               for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                   user = snapshot.getValue(User.class);
               }

                if(user!=null) {
                    username = user.getName();
                    imLeader = user.getLeader();


                    if (username == null) {
                        Intent intent = new Intent(LodingActivity.this, PersonalInfoActivity.class);
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
                if(user == null) {
                    Intent intent = new Intent(LodingActivity.this, PersonalInfoActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });











    }



}




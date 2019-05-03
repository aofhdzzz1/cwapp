package chahyunbin.cwapp1;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Member;
import java.util.ArrayList;

import chahyunbin.cwapp1.AdminMember.SingleAdapter;
import chahyunbin.cwapp1.BottomBar.UserAdapter;
import chahyunbin.cwapp1.MainActivity.LeaderMainActivity;
import chahyunbin.cwapp1.MainActivity.MemberMainActivity;
import chahyunbin.cwapp1.model.Person;
import chahyunbin.cwapp1.model.User;

import static com.facebook.FacebookSdk.getApplicationContext;

public class MemberCheck extends Activity {


    SwipeMenuListView listView;


    Button button;
    final String TAG = "Check";
    private int CALL_PERMISSION_CODE = 1;
    Activity activity;

    String name, phone, age, month, day;
    public String key;

    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<User> list;
    SingleAdapter adapter;
    String myCell;
    String leaderemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_check);


        listView = (SwipeMenuListView)findViewById(R.id.listView);


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        list = new ArrayList<>();
        adapter = new SingleAdapter();
        //리더의 이름 구하기
        ref.child("User/" + MemberMainActivity.email + "/UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                myCell = user.getMycell();
                Log.d("Check", "my cell : " + myCell);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //리더의 이메일 구하기

        Query query = ref.child("User").orderByChild("Name");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    User user = snapshot.child("UserInfo").getValue(User.class);

                    if (user.getName().equals(myCell))
                    {
                        leaderemail = snapshot.getKey().trim();

                        ref.child("User/"+leaderemail+"/Members").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Log.d(TAG, "onDataChange: ");
                                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    User user = snapshot.getValue(User.class);
                                    user.Key = snapshot.getKey();
                                    list.add(user);
                                    adapter.add(user);
                                    Log.d(TAG, "list : "+user.getName());
                                }

                                listView.setAdapter(adapter);
                                if(adapter.getCount()==0){
                                    Toast.makeText(MemberCheck.this, "리더를 도와 셀원을 늘립시다!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                        }
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });





        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "delete" item
                SwipeMenuItem callItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                callItem.setBackground(new ColorDrawable(Color.rgb(0x04,
                        0xb4, 0x04)));
                // set item width
                callItem.setWidth(300);
                // set a icon
                callItem.setIcon(R.drawable.ic_phone);

                // add to menu
                menu.addMenuItem(callItem);
            }
        };

        listView.setMenuCreator(creator);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                Log.d(TAG, "position = " + position);
                switch (index) {

                    case 0:
                        // phone
                        String phonenumber = adapter.getItemPhone(position);
                        Log.d("Firebase", "phone : " + phonenumber);
                        String tel = "tel:" + phonenumber;
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(tel));
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getApplicationContext(), "설정에서 전화 걸기 권한을 부여해주세요.", Toast.LENGTH_SHORT).show();
                        }
                }
                // false : close the menu; true : not close the menu
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = adapter.getItem_Name(i);
                Toast.makeText(getApplicationContext(), "선택" + name, Toast.LENGTH_SHORT).show();

            }
        });
    }
}
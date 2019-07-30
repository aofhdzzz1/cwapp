package chahyunbin.cwapp1.BottomBar;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
import com.google.firebase.database.annotations.Nullable;

import java.util.ArrayList;

import chahyunbin.cwapp1.MainActivity.LeaderMainActivity;
import chahyunbin.cwapp1.R;
import chahyunbin.cwapp1.model.User;

import static com.facebook.FacebookSdk.getApplicationContext;

public class NetMemberFregment extends Fragment {


    SwipeMenuListView listView;


    Button button;
    final String TAG = "NetMember";
    private int CALL_PERMISSION_CODE = 1;
    Activity activity;

    String name, phone, age, month, day;
    public String key;

    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<User> list;
    UserAdapter adapter;
    String myCell;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.adminmember, container, false);


        listView = (SwipeMenuListView) view.findViewById(R.id.listView);


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        list = new ArrayList<>();
        adapter = new UserAdapter();

        ref.child("User/" + LeaderMainActivity.email +"/UserInfo").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);
                myCell = user.getMycell();
                Log.d(TAG, "myCell = "+ myCell);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Query query =ref.child("User").orderByChild("Mycell").equalTo(myCell);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.child("UserInfo").getValue(User.class);
                    if(user.getMycell().equals(myCell))
                    {

                        if(!user.getName().equals(LeaderMainActivity.username)) {
                            Log.d(TAG, "divide myCell = "+ myCell);
                            Log.d(TAG, "so my cell is ... " + user.getMycell());
                            list.add(user);
                            adapter.add(user);
                        }
                    }
                }
//                if (adapter.getCount() == 0) {
//                    Toast.makeText(getActivity(), "셀이 없어?", Toast.LENGTH_SHORT).show();
//                }
                listView.setAdapter(adapter);
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
                        getActivity());
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
                Toast.makeText(getActivity(), "선택" + name, Toast.LENGTH_SHORT).show();

            }
        });

        return view;
    }

}


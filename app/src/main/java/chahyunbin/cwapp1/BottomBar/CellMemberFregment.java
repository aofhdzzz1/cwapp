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
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import chahyunbin.cwapp1.AdminMember.SingleAdapter;
import chahyunbin.cwapp1.MainActivity.LeaderMainActivity;

import chahyunbin.cwapp1.MemberInfo.MemberInfoActivity;
import chahyunbin.cwapp1.R;
import chahyunbin.cwapp1.model.Person;
import chahyunbin.cwapp1.model.User;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CellMemberFregment extends Fragment {


    SwipeMenuListView listView;


    Button button;
    final String TAG = "CellMember";
    private int CALL_PERMISSION_CODE = 1;
    Activity activity;

    String name, phone, age, month, day;
    public String key;

    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<User> list;
    SingleAdapter adapter;
    Person person;
    FirebaseUser user;


    public static String cellmemberInfo;
    private String username;
    private String email, emaildata;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.adminmember, container, false);


        user = FirebaseAuth.getInstance().getCurrentUser();
        //get email
        emaildata = user.getEmail();
        if (emaildata != null) {
            email = emaildata.substring(0, emaildata.indexOf("@"));

        }

        GetUserName();
        listView = (SwipeMenuListView) view.findViewById(R.id.listView);


        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        list = new ArrayList<>();
        adapter = new SingleAdapter();

        //리더 이름구하기
        ref.child("User").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.child("UserInfo").getValue(User.class);
                    if (user.getMycell().equals(username)) {
                        Log.d(TAG, "username :"+username);
                        list.add(user);
                        adapter.add(user);
                    }
                }
                if (adapter.getCount() == 0) {
                    Toast.makeText(getActivity(), "셀원을 등록시켜주세요", Toast.LENGTH_SHORT).show();
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getActivity());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xdf, 0x01,
                        0x3a)));
                // set item width
                deleteItem.setWidth(300);
                // set item title

                // set item title font color
                deleteItem.setIcon(R.drawable.ic_delete);
                // add to menu
                menu.addMenuItem(deleteItem);

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
                        // delete

                        User person = list.get(position);
                        list.remove(position);
                        ref.child("User/" + LeaderMainActivity.email + "/Members").child(person.Key).removeValue();
                        Toast.makeText(getActivity(), "성공적으로 삭제되었습니다", Toast.LENGTH_SHORT).show();
                        break;
                    case 1:
                        // phone
                        String phonenumber = adapter.getItemPhone(position);
                        Log.d("Firebase", "phone : " + phonenumber);
                        String tel = "tel:" + phonenumber;
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(tel));
                        try {
                            startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "설정에서 전화 걸기 권한을 부여해주세요.", Toast.LENGTH_SHORT).show();
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
                cellmemberInfo = name;
                Toast.makeText(getActivity(), "선택" + name, Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MemberInfoActivity.class));

            }
        });

        return view;
    }


    private void GetUserName() {
        Query query = FirebaseDatabase.getInstance().getReference("User/" + email).orderByChild("UserInfo");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    username = user.getName();
                    Log.d("Firebase", "username1 : " + username);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }


//    private void loadAllFromDB() {
//        ArrayList<Person> people = peopleTable.loadByDate(true);
//        for (Person person : people) {
//
//            adapter.add(person);
//        }
//        adapter.notifyDataSetChanged();
//    }


        });
    }
}

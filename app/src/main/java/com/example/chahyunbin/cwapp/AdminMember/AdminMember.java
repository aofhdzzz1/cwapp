package com.example.chahyunbin.cwapp.AdminMember;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.chahyunbin.cwapp.Database.PeopleTable;
import com.example.chahyunbin.cwapp.MainActivity;
import com.example.chahyunbin.cwapp.R;
import com.example.chahyunbin.cwapp.model.Person;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.security.auth.callback.Callback;

public class AdminMember extends Activity {
    SwipeMenuListView listView;


    Button button;
    final String TAG = "BookDatabase";

    Activity activity;

    String name, phone, age, month, day;


    FirebaseDatabase database;
    DatabaseReference ref;
    ArrayList<Person> list;
    SingleAdapter adapter;
    Person person;

    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminmember);

        listView = (SwipeMenuListView)findViewById(R.id.listView);

        Button backbutton = (Button) findViewById(R.id.backbutton);

        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
        database = FirebaseDatabase.getInstance();
        ref = database.getReference();
        list = new ArrayList<>();
        adapter = new SingleAdapter();

        ref.child("User/"+MainActivity.email+"/Members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                adapter.clear();
               
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Person person = snapshot.getValue(Person.class);
                    adapter.add(person);
                }
                if(adapter.getCount() == 0){
                    Toast.makeText(getApplicationContext(), "셀원을 입력해주세요", Toast.LENGTH_SHORT).show();
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



        SwipeMenuCreator creator  = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {
                // create "open" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
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
                        // delete


                        break;
                    case 1:
                        // phone
                        String phonenumber = adapter.getItemPhone(position);
                        String tel = "tel:"+phonenumber;
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(tel));
                        try {
                            startActivity(intent);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;
                }
                // false : close the menu; true : not close the menu
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
               String name = adapter.getItem_Name(i);
               Toast.makeText(AdminMember.this, "선택" + name, Toast.LENGTH_SHORT).show();

            }
        });


    }

//    private void loadAllFromDB() {
//        ArrayList<Person> people = peopleTable.loadByDate(true);
//        for (Person person : people) {
//
//            adapter.add(person);
//        }
//        adapter.notifyDataSetChanged();
//    }


}


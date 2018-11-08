package com.example.chahyunbin.cwapp.AdminMember;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.chahyunbin.cwapp.Database.PeopleTable;
import com.example.chahyunbin.cwapp.R;
import com.example.chahyunbin.cwapp.model.Person;

import java.util.ArrayList;

public class AdminMember extends Activity {
    SwipeMenuListView listView;
    SingleAdapter adapter;


    Button button;
    final String TAG = "BookDatabase";

    Activity activity;


    private PeopleTable peopleTable;




    @SuppressLint("WrongViewCast")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.adminmember);





        adapter = new SingleAdapter();
        listView = (SwipeMenuListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);


        peopleTable = PeopleTable.instance(getApplicationContext());
        if (adapter.getCount() == 0)
            loadAllFromDB();
        //


        SwipeMenuCreator creator = new SwipeMenuCreator() {

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


                        Log.d("log", "position = " + position);

                        int ad = (int)adapter.getItemId(position);
                        Log.d(TAG, "-1");
                        PeopleTable.deleteById(ad);
                        Log.d(TAG, "-2");

                        adapter.delete(position);
                        Log.d(TAG, "-3");
                        adapter.clear();
                        loadAllFromDB();
                        break;
                    case 1:
                        // phone
                        String phonenumber = peopleTable.call(position);
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse(phonenumber));
                        startActivity(intent);
                        break;
                }
                // false : close the menu; true : not close the menu
                return true;
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Person item = (Person) adapter.getItem(i);
                Toast.makeText(AdminMember.this, "선택" + item.name, Toast.LENGTH_SHORT).show();

            }
        });


    }

    private void loadAllFromDB() {
        ArrayList<Person> people = peopleTable.loadByDate(true);
        for (Person person : people) {

            adapter.add(person);
        }
        adapter.notifyDataSetChanged();
    }


}


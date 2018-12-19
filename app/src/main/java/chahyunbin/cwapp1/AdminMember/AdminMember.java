package chahyunbin.cwapp1.AdminMember;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import chahyunbin.cwapp1.MainActivity;

import chahyunbin.cwapp1.R;
import chahyunbin.cwapp1.model.Person;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AdminMember extends Activity {




    SwipeMenuListView listView;


    Button button;
    final String TAG = "BookDatabase";
    private int CALL_PERMISSION_CODE=1;
    Activity activity;

    String name, phone, age, month, day;
    public String key;

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

        if(ContextCompat.checkSelfPermission(AdminMember.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){

        }else{
            requestCallPermission();
        }



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
                    person.Key = snapshot.getKey();
                    list.add(person);
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

                        Person person = list.get(position);
                        list.remove(position);
                        ref.child("User/"+MainActivity.email+"/Members").child(person.Key).removeValue();
                        break;
                    case 1:
                        // phone
                        String phonenumber = adapter.getItemPhone(position);
                        Log.d("Firebase", "phone : "+ phonenumber);
                        String tel = "tel:"+phonenumber;
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        intent.setData(Uri.parse(tel));
                        try {
                            startActivity(intent);
                        }catch (Exception e){
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
               Toast.makeText(AdminMember.this, "선택" + name, Toast.LENGTH_SHORT).show();

            }
        });


    }
    private void requestCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(AdminMember.this,
                                    new String[] {Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALL_PERMISSION_CODE)  {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
            }
        }
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


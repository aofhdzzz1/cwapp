package com.example.chahyunbin.cwapp;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.chahyunbin.cwapp.Login.EmailSignIn;
import com.example.chahyunbin.cwapp.Login.LoginHome;
import com.example.chahyunbin.cwapp.model.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class Personal_Info extends Activity implements View.OnClickListener {

    EditText userNameInput, userPhoneInput, userAgeInput, userDayInput, userMonthInput;
    CheckBox checkLeader, checkCellMember;
    public static String imleader ;

    String userName, userPhone, userAge, userMonth, userDay;
    public static String spinerval;
    String email;

    HashMap<String, String> userInfo;
    public static Spinner cellSpinner;
    public static Query query;
    public String username;


    String emaildata;
   public static  ArrayList<String>  leaderName;
   public static  ArrayAdapter<String> adapter;

    private DatabaseReference userDatabase;
    FirebaseDatabase database;
    private boolean running = true;
    FirebaseUser user;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference().child("User");
        user = FirebaseAuth.getInstance().getCurrentUser();
        leaderName = new ArrayList<String>();
        emaildata = user.getEmail();
        Log.d("Firebase", "emaildata : "+emaildata);
        if (emaildata != null)
            email = emaildata.substring(0, emaildata.indexOf("@"));

        CheckUserInfo();



        userNameInput = (EditText) findViewById(R.id.userNameInput);
        userPhoneInput = (EditText) findViewById(R.id.userPhonenumberInput);
        userAgeInput = (EditText) findViewById(R.id.userAgeInput);
        userDayInput = (EditText) findViewById(R.id.userDayInput);
        userMonthInput = (EditText) findViewById(R.id.userMonthInput);
        checkLeader = (CheckBox) findViewById(R.id.leaderCheck);
        checkCellMember = (CheckBox) findViewById(R.id.cellMamberCheck);
        Button savebtn = (Button) findViewById(R.id.userbtnSaveInfo);
        cellSpinner = (Spinner) findViewById(R.id.affiliation);
        checkLeader.setOnClickListener(this);
        checkCellMember.setOnClickListener(this);
        query = FirebaseDatabase.getInstance().getReference().equalTo("Leader");
        // Get firebase Login Email


//        if(MainActivity.username == null) {
//            if (signIn.useremail != null)
//                email = signIn.useremail.substring(0, signIn.useremail.indexOf("@"));
//            else if (googlelogin.googleName != null)
//                email = googlelogin.googleName.substring(0, googlelogin.googleName.indexOf("@"));







        leaderName.clear();
        leaderName.add("목사님");
        leaderName.add("사모님");

        Spinnersetting();


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerInFo();

            }
        });



    }

    public void CheckUserInfo() {
        Query query = FirebaseDatabase.getInstance().getReference("User/" + email).orderByChild("UserInfo");
        try {
            Thread.sleep(700);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren())
                {
                    User user = snapshot.getValue(User.class);

                    username = user.getName();
                    Log.d("Firebase", "Personal_info CheckUser : "+username);
                    if(username!= null) {
                        Intent intent = new Intent(Personal_Info.this, MainActivity.class);
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
        Log.d("Firebase", "Personal_info CheckUser2 : "+username);

    }


    public void Spinnersetting(){

        Query query = databaseReference.orderByChild("Leader").equalTo("Leader");
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                        Log.d("Firebase", "아오 왜 안되냐 ");
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                        addName(dataSnapshot, leaderName);
                    adapter.notifyDataSetChanged();
                    cellSpinner.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });












//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//
//
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    User user = snapshot.getValue(User.class);
//
//                    leaderName.add(user.getName());
//                    Log.d("Firebase", "user.getName : " + user.getName());
//
//
//
//                }
//                adapter = new ArrayAdapter<>(Personal_Info.this,android.R.layout.simple_expandable_list_item_1,leaderName);
//
//                adapter.notifyDataSetChanged();
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });


        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        adapter = new ArrayAdapter<>(Personal_Info.this,android.R.layout.simple_expandable_list_item_1,leaderName);

        cellSpinner.setAdapter(adapter);
        cellSpinner.setSelection(0);

        cellSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               spinerval =  parent.getItemAtPosition(position).toString();
                Log.d("Firebase", "spinerval : "+spinerval);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
               // spinerval = null;
            }
        });
    }

    // get LeaderName & write LeaderName in leadername list
    private void addName(DataSnapshot dataSnapshot, ArrayList<String> leaderName) {
        User user = dataSnapshot.getValue(User.class);
        String name = user.getName();
        Log.d("Firebase", "addName: "+name);
        leaderName.add(name);
    }


    private void registerInFo(){
        userDatabase = FirebaseDatabase.getInstance().getReference("User/" + email).child("UserInfo");
        userName = userNameInput.getText().toString().trim();
        userPhone = userPhoneInput.getText().toString().trim();
        userAge = userAgeInput.getText().toString().trim();
        userMonth = userMonthInput.getText().toString().trim();
        userDay = userDayInput.getText().toString().trim();
        Boolean bln = true;

        if(userName.isEmpty()){
            userNameInput.setError("이름을 입력하세요");
            userNameInput.requestFocus();
            bln = false;
        }
        if(userPhone.isEmpty()){
            userPhoneInput.setError("전화번호를 입력하세요");
            userPhoneInput.requestFocus();
            bln = false;
        }
        if(userAge.isEmpty()){
            userAgeInput.setError("나이를 입력하세요");
            userAgeInput.requestFocus();
            bln = false;
        }
        if(userMonth.isEmpty()){
            userMonthInput.setError("생일(월)을 입력하세요");
            userMonthInput.requestFocus();
            bln = false;
        }
        if(userDay.isEmpty()){
            userDayInput.setError("생일(일)을 입력하세요");
            userDayInput.requestFocus();
            bln = false;
        }
       if(!checkCellMember.isChecked() && !checkLeader.isChecked()){
            checkCellMember.requestFocus();
            checkLeader.requestFocus();
           Toast.makeText(getApplicationContext(), "직분을 선택해주세요", Toast.LENGTH_SHORT).show();
           bln = false;
       }
       if(spinerval == null){
            cellSpinner.requestFocus();
           Toast.makeText(getApplicationContext(), "소속셀을 선택해주세요", Toast.LENGTH_SHORT).show();
            bln = false;
       }

       if(bln == true){
           userInfo = new HashMap<String, String>();
           userInfo.put("Name",userName);
           userInfo.put("Phone",userPhone);
           userInfo.put("Age",userAge);
           userInfo.put("Month",userMonth);
           userInfo.put("Day",userDay);
           userInfo.put("Leader",imleader);
           userInfo.put("Mycell",spinerval);

           userDatabase.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
               @Override
               public void onComplete(@NonNull Task<Void> task) {
                   if(task.isSuccessful()){
                       Toast.makeText(getApplicationContext(), userName+"님 저장되었습니다", Toast.LENGTH_SHORT).show();
                       Intent intent = new Intent(Personal_Info.this,MainActivity.class);
                       intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                       startActivity(intent);
                   }
               }
           });

       }


    }

    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.leaderCheck :
                checkCellMember.setChecked(false);

                imleader = "Leader";
                break;

            case R.id.cellMamberCheck :
                checkLeader.setChecked(false);
                imleader = "CellMember";
                break;

        }
    }



}

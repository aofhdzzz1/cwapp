package com.example.chahyunbin.cwapp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.chahyunbin.cwapp.MainActivity;
import com.example.chahyunbin.cwapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.jar.Attributes;

public class FirebaseDatabase_Input extends Activity {
    EditText nameInput, phonenumberInput, ageInput, dayInput, monthInput;
    String name, phonenumber, age, day, month;
    Boolean btn;
    Button savebtn, backbtn;
    private DatabaseReference mDatabase;
    MainActivity mainActivity;
    HashMap<String, String> personData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmember);



        nameInput = (EditText)findViewById(R.id.nameInput);
        phonenumberInput = (EditText)findViewById(R.id.phonenumberInput);
        ageInput = (EditText)findViewById(R.id.ageInput);
        dayInput = (EditText)findViewById(R.id.birthDayInput);
        monthInput = (EditText)findViewById(R.id.birthMonthInput);
        savebtn = (Button)findViewById(R.id.btnSaveInfo);
        backbtn = (Button)findViewById(R.id.btnBackInAdd);





        mDatabase = FirebaseDatabase.getInstance().getReference().child("User/" +mainActivity.email+"/Members");

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regesterMember();


            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }

    private void regesterMember() {
        name = nameInput.getText().toString().trim();
        phonenumber = phonenumberInput.getText().toString().trim();
        age= ageInput.getText().toString().trim();
        day = dayInput.getText().toString().trim();
        month = monthInput.getText().toString().trim();
        btn = true;

        if(name.isEmpty()){
            nameInput.setError("이름을 입력하세요");
            nameInput.requestFocus();
            btn = false;
        }
        if(phonenumber.isEmpty()){
            phonenumberInput.setError("번호를 입력하세요");
            phonenumberInput.requestFocus();
            btn = false;
        }
        if(age.isEmpty()){
            ageInput.setError("나이를 입력하세요");
            ageInput.requestFocus();
            btn = false;
        }
        if(month.isEmpty()){
            monthInput.setError("생일(달)을 입력하세요");
            monthInput.requestFocus();
            btn = false;
        }
        if(day.isEmpty()){
            dayInput.setError("생일(일)을 입력하세요");
            dayInput.requestFocus();
            btn = false;
        }
        if(btn == true) {

            personData = new HashMap<String, String>();
            personData.put("Name", name);
            personData.put("Phonenumber", phonenumber);
            personData.put("Age", age);
            personData.put("Month", month);
            personData.put("Day", day);

            mDatabase.push().setValue(personData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), name + " 저장되었습니다", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}

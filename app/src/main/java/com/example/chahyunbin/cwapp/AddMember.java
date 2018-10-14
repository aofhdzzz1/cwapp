package com.example.chahyunbin.cwapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class AddMember extends Activity {
   MemberDatabase memberDatabase;
    final String DBName = "person.db";
    final int dbVersion = 1;
    String name, phonenumber;
    EditText nameInput, phonenumberInput, ageInput, birthMonthInput, birthDayInput;
    int age,month,day;
    String agei, monthi,dayi;
    public TextView textView;

    OnDataCallBack callback;
    final String TAG = "addmember";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmember);

        Button btnSaveInfo;

        memberDatabase = new MemberDatabase(this);

        btnSaveInfo = (Button)findViewById(R.id.btnSaveInfo);
        nameInput = (EditText)findViewById(R.id.nameInput);
        phonenumberInput = (EditText)findViewById(R.id.phonenumberInput);
        ageInput = (EditText)findViewById(R.id.ageInput);
        birthMonthInput = (EditText)findViewById(R.id.birthMonthInput);
        birthDayInput = (EditText)findViewById(R.id.birthDayInput);
        textView = (TextView)findViewById(R.id.textView);




    btnSaveInfo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            name = nameInput.getText().toString().trim();
            phonenumber = phonenumberInput.getText().toString().trim();

            agei = ageInput.getText().toString().trim();
            if(!"".equals(agei)) age = Integer.parseInt(agei);
            else age = 0;

            monthi = birthMonthInput.getText().toString().trim();
            if(!"".equals(monthi))  month = Integer.parseInt(monthi);
            else month = 0;

            dayi = birthDayInput.getText().toString().trim();
            if(!"".equals(dayi)) day = Integer.parseInt(dayi);
            else day = 0;

            if(name != null && phonenumber != null && age !=0 && month !=0 && day != 0)
                showDialog(name, phonenumber, age, month, day);
            else {

                if (name == null)
                    Toast.makeText(AddMember.this, "이름을 입력하세요", Toast.LENGTH_SHORT).show();
                else if (phonenumber == null)
                    Toast.makeText(AddMember.this, "번호를 입력하세요", Toast.LENGTH_SHORT).show();
                else if (age == 0)
                    Toast.makeText(AddMember.this, "나이를 입력하세요", Toast.LENGTH_SHORT).show();
                else if (month == 0)
                    Toast.makeText(AddMember.this, "월을 입력하시오", Toast.LENGTH_SHORT).show();
                else if (day == 0)
                    Toast.makeText(AddMember.this, "달을 입력하세요", Toast.LENGTH_LONG).show();
            }



        }
    });

    }

  public void showDialog(final String name, final String phonenumber, final int age, final int month, final int day)

    {

        Log.d(TAG,"name + " +name );

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("저장");
        builder.setMessage("저장하시겠습니까?");
        builder.setCancelable(false);
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                insert(name, phonenumber, age, month, day);
                Toast.makeText(AddMember.this, "저장되었습니다.", Toast.LENGTH_SHORT).show();


            }

        });
        //  builder.setPositiveButton();

        builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();

    }

    public void insert(String name, String phonenumber, int age, int month, int day){
        memberDatabase.insertRecord(name, phonenumber, age, month, day);
    }

    public void println(String msg){
        textView.append('\n'+msg);
    }
}

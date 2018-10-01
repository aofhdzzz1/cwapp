package com.example.chahyunbin.cwapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddMember extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmember);

        Button btnSaveInfo;
        final EditText nameInput, phonenumberInput, ageInput, birthMonthInput, birthDayInput;

        btnSaveInfo = (Button)findViewById(R.id.btnSaveInfo);
        nameInput = (EditText)findViewById(R.id.nameInput);
        phonenumberInput = (EditText)findViewById(R.id.phonenumberInput);
        ageInput = (EditText)findViewById(R.id.ageInput);
        birthMonthInput = (EditText)findViewById(R.id.birthMonthInput);
        birthDayInput = (EditText)findViewById(R.id.birthDayInput);
        final DBHelper dbHelper;
        final String DBName = "person.db";
        final int dbVersion = 1;
        dbHelper = new DBHelper(this,DBName,null, dbVersion);

    btnSaveInfo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            final String name = nameInput.getText().toString().trim();
            final String phonenumber = phonenumberInput.getText().toString().trim();
            final int age = Integer.parseInt(ageInput.getText().toString().trim());
            final int month = Integer.parseInt(birthMonthInput.getText().toString().trim());
            final int day = Integer.parseInt(birthDayInput.getText().toString().trim());

            AlertDialog.Builder builder =  new AlertDialog.Builder(AddMember.this);
            builder.setTitle("저장");
            builder.setMessage("저장하시겠습니까?");
            builder.setCancelable(false);
            builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    SQLiteDatabase db;
                    String sql;


                    db = dbHelper.getWritableDatabase();
                    sql = String.format("INSERT INTO person VALUES (NULL, '%s', '%s', '%d', '%d', '%d');", name, phonenumber,age,month,day);
                    db.execSQL(sql);


                }
            });
          //  builder.setPositiveButton();

            builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });





        }
    });

    }
}

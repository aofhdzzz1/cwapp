package com.example.chahyunbin.cwapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chahyunbin.cwapp.AdminMember.SingleAdapter;
import com.example.chahyunbin.cwapp.Database.PeopleTable;
import com.example.chahyunbin.cwapp.model.Person;



public class AddMember extends Activity {


    String name, phonenumber;
    EditText nameInput, phonenumberInput, ageInput, birthMonthInput, birthDayInput;
    int age,month,day;
    String agei, monthi,dayi;
    public TextView textView;
    PeopleTable peopleTable;
    private SingleAdapter adapter;


    final String TAG = "BookDatabase";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmember);

        Button btnSaveInfo;



        btnSaveInfo = (Button)findViewById(R.id.btnSaveInfo);
        nameInput = (EditText)findViewById(R.id.nameInput);
        phonenumberInput = (EditText)findViewById(R.id.phonenumberInput);
        ageInput = (EditText)findViewById(R.id.ageInput);
        birthMonthInput = (EditText)findViewById(R.id.birthMonthInput);
        birthDayInput = (EditText)findViewById(R.id.birthDayInput);
        textView = (TextView)findViewById(R.id.textView);

        peopleTable = PeopleTable.instance(getApplicationContext());




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

            if(name != null && phonenumber != null && age !=0 && month !=0 && day != 0){


                showDialog(name, phonenumber, age, month, day);
            }

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



        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("저장");
        builder.setMessage("저장하시겠습니까?");
        builder.setCancelable(false);
        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(TAG, "1");
               insertToDB();
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



   private void insertToDB() {
        Log.d(TAG, "2");
        Log.d(TAG, "name : "+name+ "phonenumber : "+ phonenumber + " age : " + age + " month : " + month + " day " + day);
        int newId = peopleTable.insert(name, phonenumber, agei, monthi, dayi);
        Log.d(TAG, "3 ");
       Log.d(TAG, "newId : " + newId);
        Person bean = new Person(newId + "", name, phonenumber, agei, monthi, dayi);
        Log.d(TAG, "4");

        SingleAdapter.insert(bean);
       Log.d(TAG, "5");

        nameInput.setText("");
       Log.d(TAG, "7");
    }



}

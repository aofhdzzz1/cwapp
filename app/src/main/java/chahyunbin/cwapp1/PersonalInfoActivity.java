package chahyunbin.cwapp1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;

import chahyunbin.cwapp1.MainActivity.LeaderMainActivity;
import chahyunbin.cwapp1.model.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class PersonalInfoActivity extends Activity implements View.OnClickListener {

    EditText userNameInput, userPhoneInput;
    TextView birthDayInput;
    CheckBox checkLeader, checkCellMember;
    public static String imleader;

    String userName, userPhone;
    String datedata;
    String saveYear;
    public static String spinerval;
    String email;

    HashMap<String, String> userInfo;
    public static Spinner cellSpinner;
    public static Query query;
    public String username;


    String emaildata;
    public ArrayList<String> leaderName;
    public static ArrayAdapter<String> adapter;

    DatabaseReference userDatabase;
    FirebaseDatabase database;
    private boolean running = true;
    FirebaseUser user;

    Date date;
    int year;
    int month;
    int day;
    int DATE_PICKER_ID = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_info);
        database = FirebaseDatabase.getInstance();
        userDatabase = database.getReference().child("User");
        user = FirebaseAuth.getInstance().getCurrentUser();
        leaderName = new ArrayList<String>();
        emaildata = user.getEmail();
        Log.d("Firebase", "emaildata : " + emaildata);
        if (emaildata != null)
            email = emaildata.substring(0, emaildata.indexOf("@"));
        

        //CheckUserInfo();


        userNameInput = (EditText) findViewById(R.id.userNameInput);
        userPhoneInput = (EditText) findViewById(R.id.userPhonenumberInput);
        birthDayInput = (TextView) findViewById(R.id.birthDayInput);
        checkLeader = (CheckBox) findViewById(R.id.leaderCheck);
        checkCellMember = (CheckBox) findViewById(R.id.cellMamberCheck);
        Button savebtn = (Button) findViewById(R.id.userbtnSaveInfo);
        cellSpinner = (Spinner) findViewById(R.id.affiliation);
        checkLeader.setOnClickListener(this);
        checkCellMember.setOnClickListener(this);
        query = FirebaseDatabase.getInstance().getReference();
        // Get firebase Login Email


//        if(LeaderMainActivity.username == null) {
//            if (signIn.useremail != null)
//                email = signIn.useremail.substring(0, signIn.useremail.indexOf("@"));
//            else if (googlelogin.googleName != null)
//                email = googlelogin.googleName.substring(0, googlelogin.googleName.indexOf("@"));


        birthDayInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });




        ArrayList<User> list = new ArrayList<>();
        leaderName.clear();
        leaderName.add("목사님");
        leaderName.add("사모님");
        Query query = userDatabase;
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                        Log.d("PIF", "snapshot : "+snapshot);

                    User user = snapshot.child("UserInfo").getValue(User.class);

                    if (user != null) {
                        Log.d("PIF", "getLeader : " + user.getLeader());
                        if (user.getLeader().equals("Leader")) {
                            leaderName.add(user.getName());
                            Log.d("Firebase", "leaderName: " + user.getName());

                        }
                    }
                }

                Spinnersetting();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerInFo();

            }
        });


    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id){
            case 1111:
                Calendar minDate = Calendar.getInstance();
             DatePickerDialog datePickerDialog= new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, pickerListener, year, month, day);
                minDate.set(1950,1-1,01);
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());
                datePickerDialog.getDatePicker().updateDate(2000,1,1);
            return datePickerDialog;
        }
        return null;
    }




    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year  = selectedYear;
            month = selectedMonth+1;
            day   = selectedDay;

            String smonth = null,sday = null;
            if(month < 10){
                smonth ="0"+(month);
            }
            if(day < 10){
                sday = "0"+(day);
            }
            datedata = year+smonth+sday;

            birthDayInput.setText(new StringBuilder().append(year).append("년 ").append(month)
                    .append("월 ").append(day).append("일"));
        }
    };

    private void showDatePicker() {

        final Calendar c = Calendar.getInstance();
        c.set(2000,1,1);
        showDialog(DATE_PICKER_ID);


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
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    User user = snapshot.getValue(User.class);

                    username = user.getName();
                    Log.d("Firebase", "Personal_info CheckUser : " + username);
                    if (username != null) {
                        Intent intent = new Intent(PersonalInfoActivity.this, LeaderMainActivity.class);
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
        Log.d("Firebase", "Personal_info CheckUser2 : " + username);

    }


    public void Spinnersetting() {

//

        adapter = new ArrayAdapter<>(PersonalInfoActivity.this, android.R.layout.simple_expandable_list_item_1, leaderName);
        cellSpinner.setAdapter(adapter);
        cellSpinner.setSelection(0);
        cellSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinerval = parent.getItemAtPosition(position).toString();
                Log.d("Firebase", "spinerval : " + spinerval);
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
        Log.d("Firebase", "addName: " + name);
        leaderName.add(name);
    }


    private void registerInFo() {
        userDatabase = FirebaseDatabase.getInstance().getReference("User/" + email).child("UserInfo");
        userName = userNameInput.getText().toString().trim();
        userPhone = userPhoneInput.getText().toString().trim();
        Boolean bln = true;

        if (userName.isEmpty()) {
            userNameInput.setError("이름을 입력하세요");
            userNameInput.requestFocus();
            bln = false;
        }
        if (userPhone.isEmpty()) {
            userPhoneInput.setError("전화번호를 입력하세요");
            userPhoneInput.requestFocus();
            bln = false;
        }

        if (birthDayInput.equals("0000년 00월 00일")) {
            birthDayInput.setError("생일(월)을 입력하세요");
            birthDayInput.requestFocus();
            bln = false;
        }

        if (!checkCellMember.isChecked() && !checkLeader.isChecked()) {
            checkCellMember.requestFocus();
            checkLeader.requestFocus();
            Toast.makeText(getApplicationContext(), "직분을 선택해주세요", Toast.LENGTH_SHORT).show();
            bln = false;
        }
        if (spinerval == null) {
            cellSpinner.requestFocus();
            Toast.makeText(getApplicationContext(), "소속셀을 선택해주세요", Toast.LENGTH_SHORT).show();
            bln = false;
        }

        if (bln == true) {
            userInfo = new HashMap<String, String>();
            userInfo.put("Name", userName);
            userInfo.put("Phone", userPhone);
            userInfo.put("BirthDay", datedata);
            userInfo.put("Leader", imleader);
            userInfo.put("Mycell", spinerval);


            userDatabase.setValue(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), userName + "님 저장되었습니다", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(PersonalInfoActivity.this, LeaderMainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });

        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.leaderCheck:
                checkCellMember.setChecked(false);

                imleader = "Leader";
                break;

            case R.id.cellMamberCheck:
                checkLeader.setChecked(false);
                imleader = "CellMember";
                break;

        }
    }


}

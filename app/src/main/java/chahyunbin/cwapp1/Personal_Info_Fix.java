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
import java.util.HashMap;
import java.util.Map;

import chahyunbin.cwapp1.Login.LoginHomeActivity;
import chahyunbin.cwapp1.MainActivity.LeaderMainActivity;
import chahyunbin.cwapp1.model.User;

public class Personal_Info_Fix extends Activity implements View.OnClickListener {
    Button button;
    EditText userName, userPhone;
    TextView birthDayInput;
     CheckBox checkLeader, checkMember;
    String imleader;
    String datedata;

    DatabaseReference userDatabase, usersDatabase;
    FirebaseDatabase database;
    FirebaseUser user;

    Map<String, Object> userInfo;
    public static Spinner cellSpinner;
    public static Query query;
    public String username;

    String emaildata;
    public ArrayList<String> leaderName;
    public static ArrayAdapter<String> adapter;
    public static String spinerval;
    String email;

    int year, month, day;
    int DATE_PICKER_ID = 1111;
    private String suserName;
    private String suserPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal__info__fix);

        user = FirebaseAuth.getInstance().getCurrentUser();
         email = user.getEmail().substring(0, user.getEmail().indexOf("@"));
        leaderName = new ArrayList<String>();
        database = FirebaseDatabase.getInstance();
        Log.d("PIF", "email: " + email);
        userDatabase = database.getReference().child("User/" + email);
        usersDatabase = database.getReference().child("User");
        button = (Button) findViewById(R.id.userbtnSaveInfo);
        userName = (EditText) findViewById(R.id.userNameInput);
        userPhone = (EditText) findViewById(R.id.userPhonenumberInput);
        birthDayInput = (TextView) findViewById(R.id.birthDayInput);
        checkLeader = (CheckBox) findViewById(R.id.leaderCheck);
        checkMember = (CheckBox) findViewById(R.id.cellMamberCheck);
        cellSpinner = (Spinner) findViewById(R.id.affiliation);



        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user = dataSnapshot.child("UserInfo").getValue(User.class);
                Log.d("PIF", "user: " + user.getName());
                userName.setText(user.getName());
                userPhone.setText(user.getPhone());
                birthDayInput.setText(user.getBirthDay());
                year = Integer.parseInt(user.getBirthDay().substring(0, 4));
                month = Integer.parseInt(user.getBirthDay().substring(4, 6));
                day = Integer.parseInt(user.getBirthDay().substring(6, 8));
                String smonth = null, sday = null;
                birthDayInput.setText(year+"년 "+month+"월 "+day+"일");
                if(month < 10){
                    smonth = "0"+String.valueOf(month);
                }
                if(day < 10){
                    sday = "0"+String.valueOf(day);
                }
                datedata = String.valueOf(year)+smonth+sday;
                imleader = user.getLeader();

                if (user.getLeader().equals("Leader")) {
                    checkMember.setChecked(false);
                    checkLeader.setChecked(true);

                } else if (user.getLeader().equals("CellMember")) {
                    checkMember.setChecked(true);
                    checkLeader.setChecked(false);

                } else {
                    checkMember.setChecked(true);
                    checkLeader.setChecked(false);
                }



            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        checkLeader.setOnClickListener(this);
        checkMember.setOnClickListener(this);
        ArrayList<User> list = new ArrayList<>();
        leaderName.clear();
        leaderName.add("목사님");
        leaderName.add("사모님");
        Query query = usersDatabase;
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


        birthDayInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerInFo();

            }
        });


    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 1111:
                Calendar minDate = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, pickerListener, year, month, day);
                minDate.set(1950, 1 - 1, 01);
                datePickerDialog.getDatePicker().setMinDate(minDate.getTime().getTime());
                return datePickerDialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year = selectedYear;
            month = selectedMonth+1;
            day = selectedDay;

            String smonth = null, sday = null;
            if (month < 10) {
                smonth = "0" + (month);
            }
            if (day < 10) {
                sday = "0" + (day);
            }
            datedata = year + smonth + sday;


            birthDayInput.setText(new StringBuilder().append(year).append("년 ").append(month)
                    .append("월 ").append(day).append("일"));
        }
    };

    private void showDatePicker() {

        final Calendar c = Calendar.getInstance();
        c.set(2000,01,01);
        showDialog(DATE_PICKER_ID);


    }

    public void Spinnersetting() {

//

        adapter = new ArrayAdapter<>(Personal_Info_Fix.this, android.R.layout.simple_expandable_list_item_1, leaderName);
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

    private void registerInFo() {
        usersDatabase = FirebaseDatabase.getInstance().getReference("User/" + email).child("UserInfo");
        suserName = userName.getText().toString().trim();
        suserPhone = userPhone.getText().toString().trim();
        Boolean bln = true;

        if (suserName.isEmpty()) {
            userName.setError("이름을 입력하세요");
            userName.requestFocus();
            bln = false;
        }
        if (suserPhone.isEmpty()) {
            userPhone.setError("전화번호를 입력하세요");
            userPhone.requestFocus();
            bln = false;
        }

        if (birthDayInput.equals("0000년 00월 00일")) {
            birthDayInput.setError("생일(월)을 입력하세요");
            birthDayInput.requestFocus();
            bln = false;
        }

        if (!checkMember.isChecked() && !checkLeader.isChecked()) {
            checkMember.requestFocus();
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
            userInfo = new HashMap<String, Object>();
            userInfo.put("Name", suserName);
            userInfo.put("Phone", suserPhone);
            userInfo.put("BirthDay", datedata);
            userInfo.put("Leader", imleader);
            userInfo.put("Mycell", spinerval);


            usersDatabase.updateChildren(userInfo).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(getApplicationContext(), "성공적으로 수정되었습니다", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(Personal_Info_Fix.this, LoginHomeActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                    }
                }
            });
        }
    }
        @Override
        public void onClick (View v){
            switch (v.getId()) {
                case R.id.leaderCheck:
                    checkMember.setChecked(false);

                    imleader = "Leader";
                    break;

                case R.id.cellMamberCheck:
                    checkLeader.setChecked(false);
                    imleader = "CellMember";
                    break;

            }
        }
    }

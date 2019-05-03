package chahyunbin.cwapp1;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;


import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

import chahyunbin.cwapp1.model.User;

public class Personal_Info_Fix extends Activity {
    Button button;
    EditText name, phone;
    TextView birthDay;
    CheckBox checkLeader, checkMember;
    public static String imleader;

    DatabaseReference userDatabase;
    FirebaseDatabase database;
    FirebaseUser user;

    String email;

    int year, month, day;
    int DATE_PICKER_ID = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal__info__fix);

        user = FirebaseAuth.getInstance().getCurrentUser();
        String email = user.getEmail().substring(0,user.getEmail().indexOf("@"));

        database = FirebaseDatabase.getInstance();
        Log.d("PIF", "email: "+email);
        userDatabase = database.getReference().child("User/"+email);

        button = (Button)findViewById(R.id.userbtnSaveInfo);
        name = (EditText)findViewById(R.id.userNameInput);
        phone = (EditText)findViewById(R.id.userPhonenumberInput);
        birthDay = (TextView)findViewById(R.id.birthDayInput);
        checkLeader = (CheckBox)findViewById(R.id.leaderCheck);
        checkMember = (CheckBox)findViewById(R.id.cellMamberCheck);



        userDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    User user = dataSnapshot.child("UserInfo").getValue(User.class);
                    Log.d("PIF", "user: "+user.getName());
                    name.setText(user.getName());
                    phone.setText(user.getPhone());
                    birthDay.setText(user.getBirthDay());
                    year = Integer.parseInt(user.getBirthDay().substring(0,4));
                    month = Integer.parseInt(user.getBirthDay().substring(6,7));
                    day = Integer.parseInt(user.getBirthDay().substring(9,10));

                Log.d("PIF", "gyear, month, day :"+year+month+day);
                    if(user.getLeader().equals("Leader")) {
                        checkLeader.setChecked(true);
                    }else
                        checkMember.setChecked(false);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        
        birthDay.setOnClickListener(new View.OnClickListener() {
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

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch(id){
            case 1111:

                return new DatePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT, pickerListener, year, month-1, day);
        }
        return null;
    }
    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;

            birthDay.setText(new StringBuilder().append(year).append("년 ").append(month + 1)
                    .append("월 ").append(day).append("일"));
        }
    };

    private void showDatePicker() {

        final Calendar c = Calendar.getInstance();
        showDialog(DATE_PICKER_ID);


    }
}

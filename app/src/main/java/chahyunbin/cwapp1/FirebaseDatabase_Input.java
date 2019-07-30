package chahyunbin.cwapp1;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;

import chahyunbin.cwapp1.MainActivity.LeaderMainActivity;

public class FirebaseDatabase_Input extends Activity {
    EditText nameInput, phonenumberInput, ageInput;
    TextView birthdayInput;
    String name, phonenumber, age, datedata;
    Boolean btn;
    Button savebtn, backbtn;
    private DatabaseReference mDatabase;
    LeaderMainActivity leaderMainActivity;
    HashMap<String, String> personData;
    long now;
    Date date;

    String saveYear;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addmember);



        nameInput = (EditText)findViewById(R.id.nameInput);
        phonenumberInput = (EditText)findViewById(R.id.phonenumberInput);
        ageInput = (EditText)findViewById(R.id.ageInput);
        birthdayInput = (TextView) findViewById(R.id.birthDayInput);
        savebtn = (Button)findViewById(R.id.btnSaveInfo);
        backbtn = (Button)findViewById(R.id.btnBackInAdd);

        birthdayInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        now = System.currentTimeMillis();
        date = new Date(now);
        saveYear =  String.valueOf(date.getYear());



        mDatabase = FirebaseDatabase.getInstance().getReference().child("User/" + leaderMainActivity.email+"/Members");

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regesterMember();


            }
        });
        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeaderMainActivity.class);
                startActivity(intent);
                finish();
            }
        });




    }

    private void regesterMember() {
        name = nameInput.getText().toString().trim();
        phonenumber = phonenumberInput.getText().toString().trim();
        age= ageInput.getText().toString().trim();


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
        if(birthdayInput.equals("00월 00일")){
            birthdayInput.setError("생일을 입력하세요");
            birthdayInput.requestFocus();
            btn = false;
        }

        if(btn == true) {

            personData = new HashMap<String, String>();
            personData.put("Name", name);
            personData.put("Phonenumber", phonenumber);
            personData.put("Age", age);
            personData.put("Birthday", datedata);
            personData.put("SaveYear",saveYear);


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
    private void showDatePicker() {
        new SingleDateAndTimePickerDialog.Builder(FirebaseDatabase_Input.this)
                .bottomSheet()
                .curved()
                .displayMinutes(false)
                .displayHours(false)
                .displayDays(false)
                .displayYears(false)
                .displayDaysOfMonth(true)
                .displayMonth(true)
                .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                    @Override
                    public void onDisplayed(SingleDateAndTimePicker picker) {
                        //retrieve the SingleDateAndTimePicker
                        Log.d("Date", "onDisplayed : "+String.valueOf(picker));
                    }
                })

                .title("생일")
                .listener(new SingleDateAndTimePickerDialog.Listener() {
                    @Override
                    public void onDateSelected(Date date) {
                        Log.d("Date", "onDateSelected : "+String.valueOf(date));
                        datedata = date.getMonth()+1+"월 "+date.getDate()+"일";
                        birthdayInput.setText(String.valueOf(datedata));
                    }
                }).display();

    }
}

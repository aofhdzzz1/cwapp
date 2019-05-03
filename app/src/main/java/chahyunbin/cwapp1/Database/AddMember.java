package chahyunbin.cwapp1.Database;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;

import java.util.Date;

import chahyunbin.cwapp1.AdminMember.SingleAdapter;
import chahyunbin.cwapp1.MainActivity.LeaderMainActivity;

import chahyunbin.cwapp1.Personal_Info;
import chahyunbin.cwapp1.R;
import chahyunbin.cwapp1.model.Person;
import chahyunbin.cwapp1.model.User;


public class AddMember extends Activity {


    String name, phonenumber;
    EditText nameInput, phonenumberInput, ageInput;
    TextView birthdayInput;
    Button backbutton;
    int age,month,day;
    String agei, monthi,dayi;
    public TextView textView;
    PeopleTable peopleTable;
    private SingleAdapter adapter;
    String datedata;

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
        birthdayInput = (TextView)findViewById(R.id.birthDayInput);

        backbutton = (Button)findViewById(R.id.btnBackInAdd);


        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), LeaderMainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        peopleTable = PeopleTable.instance(getApplicationContext());

        birthdayInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });




    btnSaveInfo.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {





            name = nameInput.getText().toString().trim();
            phonenumber = phonenumberInput.getText().toString().trim();

            agei = ageInput.getText().toString().trim();
            if(!"".equals(agei)) age = Integer.parseInt(agei);
            else age = 0;



            if(name.isEmpty()){
                nameInput.setError("Name is required");
                nameInput.requestFocus();
            }
            if(phonenumber.isEmpty()){
                phonenumberInput.setError("Phonenumber is required");
                phonenumberInput.requestFocus();
            }
            if(agei.isEmpty()){
                ageInput.setError("Name is required");
                ageInput.requestFocus();
            }
            if(birthdayInput.equals("00월 00일")){
                birthdayInput.setError("Phonenumber is required");
                birthdayInput.requestFocus();
            }

            if(name != null && phonenumber != null && age !=0 && month !=0 && day != 0){


                showDialog(name, phonenumber, age, month, day);
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

    private void showDatePicker() {
        new SingleDateAndTimePickerDialog.Builder(AddMember.this)
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

   private void insertToDB() {


        int newId = peopleTable.insert(name, phonenumber, agei, datedata);


        User bean = new User(newId + "", name, phonenumber, agei, datedata);


        SingleAdapter.insert(bean);


        nameInput.setText("");

    }



}

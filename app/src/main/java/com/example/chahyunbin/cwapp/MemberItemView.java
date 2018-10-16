package com.example.chahyunbin.cwapp;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MemberItemView extends LinearLayout {
    TextView nameText, ageText, birthText, phoneText;

    public MemberItemView(Context context) {
        super(context);

        info(context);
    }

    public MemberItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        info(context);

    }

    public void info(Context context) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.member_item,this,true);

        nameText = (TextView)findViewById(R.id.nameView);
        ageText = (TextView)findViewById(R.id.ageView);
        birthText = (TextView)findViewById(R.id.birthView);
        phoneText = (TextView)findViewById(R.id.phonenumberView);

    }

    public void setName(String name){
        nameText.setText(name);

    }
    public void setAge(String age){
        ageText.setText(age);

    }
    public void setBirth(String birth){
        birthText.setText(birth);

    }
    public void setPhone(String phone){
        phoneText.setText(phone);
    }
}

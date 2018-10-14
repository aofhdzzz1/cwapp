package com.example.chahyunbin.cwapp;

import java.util.ArrayList;

public interface OnDataCallBack {
    public void insert(String name, String phonenumber, int age, int month, int day);
    public ArrayList<MemberItem> selectAll();
}

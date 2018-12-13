package com.example.chahyunbin.cwapp.model;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.Map;

public class Person {
    public  String id;
    private  String Name;
    private  String Phonenumber;
    private  String Age;
    private  String Month;
    private  String Day;
    private Map<String, String> mp;
     public String Key;

    public Person(String id, String name, String phonenumber,String age, String month, String day) {
        super();
        this.id = id;
        this.Name = name;
        this.Phonenumber = phonenumber;
        this.Age = age;
        this.Month = month;
        this.Day = day;
    }
    public Person(String name, String phonenumber,String age, String month, String day) {
        super();
        this.Name = name;
        this.Phonenumber = phonenumber;
        this.Age = age;
        this.Month = month;
        this.Day = day;
    }

    public Person() {

    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getPhonenumber() {
        return Phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        Phonenumber = phonenumber;
    }

    public String getAge() {
        return Age;
    }

    public void setAge(String age) {
        Age = age;
    }

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public String getDay() {
        return Day;
    }
    @Exclude
    public String getmKey() {
        return Key;
    }
    @Exclude
    public void setmKey(String mKey) {
        this.Key = mKey;
    }

    public void setDay(String day) {
        Day = day;
    }
    public Map<String, String> toMap() {
        mp = new HashMap<>();
        mp.put("Name", this.Name);
        mp.put("Phonenumber", this.Phonenumber);
        mp.put("Age", this.Age);

        mp.put("Month",this.Month);
        mp.put("Day",this.Day);


        return mp;
    }
}

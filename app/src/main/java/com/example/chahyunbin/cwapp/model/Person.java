package com.example.chahyunbin.cwapp.model;

public class Person {
    public String id;
    public String name;
    public String phonenumber;
    public String age;
    public String month;
    public String day;

    public Person(String id, String name, String phonenumber,String age, String month, String day) {
        super();
        this.id = id;
        this.name = name;
        this.phonenumber = phonenumber;
        this.age = age;
        this.month = month;
        this.day = day;
    }
    public Person(String name, String phonenumber,String age, String month, String day) {
        super();
        this.name = name;
        this.phonenumber = phonenumber;
        this.age = age;
        this.month = month;
        this.day = day;
    }
}

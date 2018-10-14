package com.example.chahyunbin.cwapp;

public class MemberItem {

    String name;
    String phone;
    int age;
    String birth;

    public MemberItem(String name, String phone, int age, String birth) {
        this.name = name;
        this.age = age;
        this.birth = birth;
        this.phone = phone;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBirth() {
        return birth;
    }

    public void setBirth(String birth) {
        this.birth = birth;
    }
}

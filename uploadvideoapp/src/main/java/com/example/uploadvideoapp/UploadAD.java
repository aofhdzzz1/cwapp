package com.example.uploadvideoapp;


public class UploadAD {
    private String mName;
    private String mADUri;


    public UploadAD(String name, String aduri) {
        if (name.trim().equals("")) {
            name = "No Name";

        }


        mName = name;
        mADUri = aduri;

    }
    public UploadAD(){

    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }


    public String getmADUri() {
        return mADUri;
    }

    public void setmADUri(String mADUri) {
        this.mADUri = mADUri;
    }
}

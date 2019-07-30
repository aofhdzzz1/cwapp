package com.example.uploadvideoapp;

public class UploadVideo {
    private String mName;
    private String mVideoUri;
    private String mVideoDate;


    public UploadVideo(String name, String videoUri, String VideoDate) {
        if (name.trim().equals("")) {
            name = "No Name";

        }


        mName = name;
        mVideoUri = videoUri;
        mVideoDate = VideoDate;

    }

    public UploadVideo(){

    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }


    public String getmVideoUri() {
        return mVideoUri;
    }

    public void setmVideoUri(String mVideoUri) {
        this.mVideoUri = mVideoUri;
    }

    public String getmVideoDate() {
        return mVideoDate;
    }

    public void setmVideoDate(String mVideoDate) {
        this.mVideoDate = mVideoDate;
    }
}

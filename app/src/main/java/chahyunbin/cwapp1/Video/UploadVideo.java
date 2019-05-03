package chahyunbin.cwapp1.Video;

public class UploadVideo {
    private String mName;
    private String mVideoUri;


    public UploadVideo(String name, String videoUri) {
        if (name.trim().equals("")) {
            name = "No Name";

        }


        mName = name;
        mVideoUri = videoUri;

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
}

package chahyunbin.cwapp1.Video;


public class UploadImage {
    private String mName;
    private String mImageUri;


    public UploadImage(String name, String imageUri) {
        if (name.trim().equals("")) {
            name = "No Name";

        }



        mName = name;
        mImageUri = imageUri;

    }
    public UploadImage(){

    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }


    public String getmImageUri() {
        return mImageUri;
    }

    public void setmImageUri(String mImageUri) {
        this.mImageUri = mImageUri;
    }
}

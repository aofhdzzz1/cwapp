package chahyunbin.cwapp1.MainActivity;

public class AD {
    private String mName;
    private String mADUri;

    public AD(String mName, String mADUri) {

        this.mName = mName;
        this.mADUri = mADUri;
    }

    public AD() {
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

package chahyunbin.cwapp1.MainActivity;

import java.util.List;

public interface AdFirebaseLoad {
    void onFirebaseLoadSuccess(List<AD> mAdList);
    void onFirebaseLoadFailed(String massage);
}

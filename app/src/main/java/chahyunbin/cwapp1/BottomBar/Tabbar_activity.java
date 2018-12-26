package chahyunbin.cwapp1.BottomBar;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;


import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;


import chahyunbin.cwapp1.MainActivity.LeaderMainActivity;
import chahyunbin.cwapp1.R;

public class Tabbar_activity extends FragmentActivity {
    String TAG = "bar";


    BottomBar bottomBar;
    private int CALL_PERMISSION_CODE=1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.tabbar_activity);

        if(ContextCompat.checkSelfPermission(Tabbar_activity.this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED){

        }else{
            requestCallPermission();
        }

        initFragment();

        bottomBar = (BottomBar)findViewById(R.id.bottomBar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {

                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

                switch (tabId){
                    case  R.id.netMember :
                        transaction.replace(R.id.contentContainer,new NetMemberFregment()).commit();
                        Log.d(TAG, "1");
                        break;
                    case R.id.cellMember :
                        transaction.replace(R.id.contentContainer,new CellMemberFregment()).commit();
                        Log.d(TAG, "2");
                        break;
                    case R.id.monitoring :
                        transaction.replace(R.id.contentContainer,new MonitoringFregment()).commit();
                        Log.d(TAG, "3");
                        break;
                }
            }
        });







//        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
//
//        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
//            @Override
//            public void onTabSelected(@IdRes int tabId) {
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                if (tabId == R.id.netMember) {
//                    transaction.replace(R.id.contentContainer, new NetMemberFregment());
//                    Log.d(TAG, "first fragment");
//
//                }
//                if (tabId == R.id.cellMember) {
//                    transaction.replace(R.id.contentContainer, new CellMemberFregment());
//                    Log.d(TAG, "second fragment");
//                }
//                if (tabId == R.id.monitoring) {
//                    transaction.replace(R.id.contentContainer, new MonitoringFregment());
//                    Log.d(TAG, "third fragment");
//                }
//            }
//        });

    }

    public void initFragment(){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.contentContainer,new CellMemberFregment());
        transaction.addToBackStack(null);
        transaction.commit();
    }



    private void requestCallPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CALL_PHONE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Tabbar_activity.this,
                                    new String[] {Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.CALL_PHONE}, CALL_PERMISSION_CODE);
        }
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CALL_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
            }
        }
    }


}

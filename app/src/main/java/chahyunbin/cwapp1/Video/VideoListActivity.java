package chahyunbin.cwapp1.Video;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;

import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import chahyunbin.cwapp1.R;

import static android.os.Environment.DIRECTORY_DCIM;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class VideoListActivity extends AppCompatActivity implements VideoAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<UploadImage> mUploadImages;
    private List<UploadVideo> mUploadVideo;
    private StorageReference videoRef;

    String TAG = "Mainac";
    private StorageReference ref;

    private int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_videos);

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mProgressCircle = findViewById(R.id.progress_circle);

        mUploadImages = new ArrayList<>();
        mUploadVideo = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Uploads");



        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploadImages.clear();
                mUploadVideo.clear();
                if(dataSnapshot.getChildren() != null)
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(snapshot.child("(image)").getValue(UploadImage.class) != null ||snapshot.child("(video)").getValue(UploadVideo.class) !=null ) {
                        UploadImage uploadImage = snapshot.child("(image)").getValue(UploadImage.class);
                        Log.d(TAG, "Image uri: "+uploadImage.getmImageUri());
                        UploadVideo uploadVideo = snapshot.child("(video)").getValue(UploadVideo.class);
                        Log.d(TAG, "Video uri : "+uploadVideo.getmVideoUri());
                        mUploadImages.add(uploadImage);
                        mUploadVideo.add(uploadVideo);
                    }



                }

                mAdapter = new VideoAdapter(VideoListActivity.this, mUploadImages);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(VideoListActivity.this);
                mProgressCircle.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VideoListActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onItemClick(int position) {

        DialogSimple(position);
    }

    @Override
    public void onDownloadClick(int position) {

        final String filename = mUploadVideo.get(position).getmName();
        Toast.makeText(this, "Download File : "+mUploadVideo.get(position).getmName(), Toast.LENGTH_SHORT).show();
        videoRef = FirebaseStorage.getInstance().getReference();
        ref = videoRef.child("Uploads/Video"+mUploadVideo.get(position).getmName());


        videoRef.child("Uploads/Video/"+filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                downloadFile(VideoListActivity.this,filename,"mp4",DIRECTORY_DOWNLOADS,uri.toString());
                Toast.makeText(VideoListActivity.this, "Download Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(VideoListActivity.this, "Download Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public String downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {


        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {

            Log.d(TAG, "downloadFile: ");
            DownloadManager downloadmanager = (DownloadManager) context.
                    getSystemService(Context.DOWNLOAD_SERVICE);
            Uri uri = Uri.parse(url);
            DownloadManager.Request request = new DownloadManager.Request(uri);

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            request.setDestinationInExternalPublicDir(destinationDirectory, fileName + fileExtension);
            Environment.getExternalStoragePublicDirectory(DIRECTORY_DCIM).mkdirs();
            downloadmanager.enqueue(request);
        } else {
            requestPermisssion();
            downloadFile(context, fileName, fileExtension, destinationDirectory, url);

        }

        return fileName + fileExtension;
    }
    public void requestPermisssion() {// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {


        if (requestCode == MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.

            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
            }
            return;
        }

        // other 'case' lines to check for other
        // permissions this app might request
    }


    public void DialogSimple(int position){
        final int po = position;
        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage("이 영상을 다운로드 하시겠습니까?").setCancelable(
                false).setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'Yes' Button
                        final String filename = mUploadVideo.get(po).getmName();
                        Toast.makeText(VideoListActivity.this, "Download File : "+mUploadVideo.get(po).getmName(), Toast.LENGTH_SHORT).show();
                        videoRef = FirebaseStorage.getInstance().getReference();
                        ref = videoRef.child("Uploads/Video"+mUploadVideo.get(po).getmName());


                        videoRef.child("Uploads/Video/"+filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                // Got the download URL for 'users/me/profile.png'

                                downloadFile(VideoListActivity.this,filename,".mp4",DIRECTORY_DOWNLOADS,uri.toString());
                                Toast.makeText(VideoListActivity.this, "다운로드가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle any errors
                                Toast.makeText(VideoListActivity.this, "Download Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Action for 'NO' Button
                        dialog.cancel();
                    }
                });
        alt_bld.show();


    }



}


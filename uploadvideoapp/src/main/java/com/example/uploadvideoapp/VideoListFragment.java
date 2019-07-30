package com.example.uploadvideoapp;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import static android.os.Environment.DIRECTORY_DCIM;

public class VideoListFragment extends Fragment implements VideoAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<UploadImage> mUploadImages;
    private List<UploadVideo> mUploadVideo;
    private StorageReference videoRef;
    private StorageReference ref;

    String TAG = "videoView";
    private String findfilename;
    int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 2;

    public static VideoListFragment newInstance() {
        VideoListFragment fragment = new VideoListFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_videos, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mProgressCircle = view.findViewById(R.id.progress_circle);

        mUploadImages = new ArrayList<>();
        mUploadVideo = new ArrayList<>();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Uploads");


        mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mUploadImages.clear();
                mUploadVideo.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    UploadImage uploadImage = snapshot.child("(image)").getValue(UploadImage.class);
                    Log.d(TAG, "Image uri: " + uploadImage.getmImageUri());
                    UploadVideo uploadVideo = snapshot.child("(video)").getValue(UploadVideo.class);
                    Log.d(TAG, "Video uri : " + uploadVideo.getmVideoUri());
                    mUploadImages.add(uploadImage);
                    mUploadVideo.add(uploadVideo);

                }
                mAdapter = new VideoAdapter(getActivity(), mUploadImages);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(VideoListFragment.this);
                mProgressCircle.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });
        return view;

    }

    @Override
    public void onItemClick(int position) {


        if (mUploadVideo.get(position).getmVideoUri() != null) {
            Log.d(TAG, "onItemClick: ");
        } else
            Toast.makeText(getActivity(), "비디오 주소가 없습니다", Toast.LENGTH_SHORT).show();


    }


    @Override
    public void onDownloadClick(int position) {

        final String filename = mUploadVideo.get(position).getmName();
        Toast.makeText(getActivity(), "Download File : " + mUploadVideo.get(position).getmName(), Toast.LENGTH_SHORT).show();
        videoRef = FirebaseStorage.getInstance().getReference();
        ref = videoRef.child("Uploads/Video" + mUploadVideo.get(position).getmName());

        videoRef.child("Uploads/Video/" + filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                findfilename = (String) downloadFileInPhone(getActivity(), filename, ".mp4", DIRECTORY_DCIM, uri.toString());
                Toast.makeText(getActivity(), "Download Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(getActivity(), "Download Fail", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onDeleteClick(int position) {


    }



    public String downloadFileInPhone(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {


        // Assume thisActivity is the current activity
        int permissionCheck = ContextCompat.checkSelfPermission(getActivity(),
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
            downloadFileInPhone(context, fileName, fileExtension, destinationDirectory, url);

        }

        return fileName + fileExtension;
    }
//권한 요청
    public void requestPermisssion() {// Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                // Show an expanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.

            } else {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(getActivity(),
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



}

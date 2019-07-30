package com.example.uploadvideoapp;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class VideosActivity extends AppCompatActivity implements VideoAdapter.OnItemClickListener {

    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<UploadImage> mUploadImages;
    private List<UploadVideo> mUploadVideo;
    private StorageReference videoRef;
    private StorageReference ref;

    String TAG = "Mainac";

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
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UploadImage uploadImage = snapshot.child("(image)").getValue(UploadImage.class);
                    Log.d(TAG, "Image uri: "+uploadImage.getmImageUri());
                    UploadVideo uploadVideo = snapshot.child("(video)").getValue(UploadVideo.class);
                    Log.d(TAG, "Video uri : "+uploadVideo.getmVideoUri());
                    mUploadImages.add(uploadImage);
                    mUploadVideo.add(uploadVideo);

                }
                mAdapter = new VideoAdapter(VideosActivity.this, mUploadImages);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(VideosActivity.this);
                mProgressCircle.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(VideosActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, VideoviewActivity.class);

        if(mUploadVideo.get(position).getmVideoUri() != null) {
            intent.putExtra("VideoUri", mUploadVideo.get(position).getmVideoUri());
            startActivity(intent);
        }else
            Toast.makeText(this, "비디오 주소가 없습니다", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onDownloadClick(int position) {

        final String filename = mUploadVideo.get(position).getmName();
        Toast.makeText(this, "Download File : "+mUploadVideo.get(position).getmName(), Toast.LENGTH_SHORT).show();
        videoRef = FirebaseStorage.getInstance().getReference();
         ref = videoRef.child("Uploads/Video"+mUploadVideo.get(position).getmName());

//        File localFile = null;
//        try {
//            localFile = File.createTempFile("video", "3gp");
//
//
//        ref.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
//            @Override
//            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
//                // Local temp file has been created
//                Toast.makeText(VideosActivity.this, "Download Success", Toast.LENGTH_SHORT).show();
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception exception) {
//                // Handle any errors
//                Toast.makeText(VideosActivity.this, "Download Fail", Toast.LENGTH_SHORT).show();
//            }
//        });
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        videoRef.child("Uploads/Video/"+filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                downloadFile(VideosActivity.this,filename,"mp4",DIRECTORY_DOWNLOADS,uri.toString());
                Toast.makeText(VideosActivity.this, "Download Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(VideosActivity.this, "Download Fail", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDeleteClick(int position) {

    }

    public void downloadFile(Context context, String fileName, String fileExtension, String destinationDirectory, String url) {


        DownloadManager downloadmanager = (DownloadManager) context.
                getSystemService(Context.DOWNLOAD_SERVICE);
        Uri uri = Uri.parse(url);
        DownloadManager.Request request = new DownloadManager.Request(uri);

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName + fileExtension);

        downloadmanager.enqueue(request);
    }



}

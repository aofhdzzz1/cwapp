package chahyunbin.cwapp1.Video;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.facebook.share.internal.VideoUploader;
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

import static android.os.Environment.DIRECTORY_DOWNLOADS;

public class Video_ListView extends Activity implements VideoAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private VideoAdapter mAdapter;

    private ProgressBar mProgressCircle;

    private DatabaseReference mDatabaseRef;
    private List<UploadImage> mUploadImages;
    private List<UploadVideo> mUploadVideo;
    private StorageReference videoRef;

    String TAG = "Mainac";
    private StorageReference ref;

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

                mAdapter = new VideoAdapter(Video_ListView.this, mUploadImages);

                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(Video_ListView.this);
                mProgressCircle.setVisibility(View.INVISIBLE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Video_ListView.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                mProgressCircle.setVisibility(View.INVISIBLE);
            }
        });

    }

    @Override
    public void onItemClick(int position) {
        Intent intent = new Intent(this, Video_View.class);

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


        videoRef.child("Uploads/Video/"+filename).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                // Got the download URL for 'users/me/profile.png'

                downloadFile(Video_ListView.this,filename,"mp4",DIRECTORY_DOWNLOADS,uri.toString());
                Toast.makeText(Video_ListView.this, "Download Success", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
                Toast.makeText(Video_ListView.this, "Download Fail", Toast.LENGTH_SHORT).show();
            }
        });
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

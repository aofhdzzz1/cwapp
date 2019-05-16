package com.example.uploadvideoapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity  {

    private static final int PICK_VIDEO_REQUEST = 3;
    private static final int PICK_IMAGE_REQUEST = 4;
    Uri mVideoUri, mImageUri;
    private MediaController mediaController;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private String videoname;
    private String TAG = "Mainac";
    String Filename;
    private StorageTask mUploadTask;

    Uri[] DownloadVideoUri;
    Uri[] DownloadImageUri;

    private StorageReference mVideoStorageRef, mImageStorageRef;

    Button mButtonChooseVideo, mButtonUpload, mButtonChooseImage;
    TextView mTextViewShowUploads;
    EditText mEditTextFileName;
    ProgressBar mProgressBar;
    VideoView mVideoView;
    ImageView mImageView;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonChooseVideo = findViewById(R.id.button_choose_video);
        mButtonUpload = findViewById(R.id.button_upload);
        mTextViewShowUploads = findViewById(R.id.text_view_show_uploads);
        mVideoView = findViewById(R.id.video_view);
        mImageView = findViewById(R.id.image_view);
        mProgressBar = findViewById(R.id.progress_bar);
        mEditTextFileName = findViewById(R.id.edit_text_file_name);
        mButtonChooseImage = findViewById(R.id.button_choose_image);

        mStorageRef = FirebaseStorage.getInstance().getReference("Uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Uploads");


        mButtonChooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        mButtonChooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileImageChooser();
            }
        });

        mButtonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(MainActivity.this, "UploadImage in progress", Toast.LENGTH_SHORT).show();
                } else {
                     Filename = mEditTextFileName.getText().toString().trim();
                    if(Filename == null){
                        Filename = "No name";
                    }else {
                        Log.d(TAG, "videoUri :" + mVideoUri + " imageUri : " + mImageUri);
                        uploadVideoFile();
                        uploadImageFile();
/*
                    여기 수정수정
 */
                        Log.d(TAG, "falisefalie");


                        Toast.makeText(MainActivity.this, "upload success", Toast.LENGTH_SHORT).show();


                    }
                }
            }
        });
        mTextViewShowUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImagesActivity();
            }
        });


    }

    private void openFileImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadImageFile() {


        //Imgae upload

        if (mImageUri != null) {
            Log.d(TAG, "2");
            mImageStorageRef = mStorageRef.child("Image/" + mEditTextFileName.getText().toString().trim());
            mImageStorageRef.putFile(mImageUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    Log.d(TAG, "then: 1");
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return mImageStorageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        Log.e(TAG, "then: " + downloadUri.toString());
                        UploadImage uploadImage = new UploadImage(Filename,downloadUri.toString());
                        mDatabaseRef.child(Filename+"/(image)").setValue(uploadImage);
                        Log.d(TAG, "ImageUri : "+downloadUri.toString());

                    } else {
                        Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        Log.d(TAG, "faile ");
                    }
                }
            });
        }

    }

    private void uploadVideoFile() {

        //Video upload
        if (mVideoUri != null) {
            Log.d(TAG, "uploadVideoFile: " + mVideoUri);
            mVideoStorageRef = mStorageRef.child("Video/" + mEditTextFileName.getText().toString().trim());
            mVideoStorageRef.putFile(mVideoUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return mVideoStorageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {

                        Uri downloadVideoUri = task.getResult();
                        Log.e("TAG", "then: " + downloadVideoUri.toString());
                        Toast.makeText(MainActivity.this, "uploadImage Video success", Toast.LENGTH_SHORT).show();

                        UploadVideo uploadVideo = new UploadVideo(Filename, downloadVideoUri.toString());
                        mDatabaseRef.child(Filename+"/(video)").setValue(uploadVideo);
                        Log.d(TAG, "VideoUri : "+downloadVideoUri.toString());

                    } else {
                        Toast.makeText(MainActivity.this, "upload failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
//                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
//                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
//                            mProgressBar.setProgress((int) progress);
//                        }
//                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "faile ");
        }

    }


    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_VIDEO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case Activity.RESULT_OK:
                switch (requestCode) {
                    case PICK_VIDEO_REQUEST:
                        if (data.getData() != null) {
                            mVideoUri = data.getData();
                            mVideoView.setVideoURI(mVideoUri);
                            videoname = getFileName(mVideoUri);
                            mVideoView.start();
                        }
                        break;
                    case PICK_IMAGE_REQUEST:
                        if (data.getData() != null) {
                            mImageUri = data.getData();
                            Log.d(TAG, "mImageUri: " + mImageUri);
                            Picasso.with(this).load(mImageUri).into(mImageView);
                        }

                }

        }

    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);

            }
        }
        return result;
    }

    private void openImagesActivity() {
        Intent intent = new Intent(this, VideosActivity.class);
        startActivity(intent);
    }


}

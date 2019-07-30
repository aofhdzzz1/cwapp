package com.example.uploadvideoapp;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
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

public class VideoFragment extends Fragment {
    private static final int PICK_VIDEO_REQUEST = 3;
    private static final int PICK_IMAGE_REQUEST = 4;
    Uri mVideoUri, mImageUri;
    private MediaController mediaController;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;
    private String videoname, date;
    private String TAG = "Mainac";
    String Filename;
    private StorageTask mUploadTask;

    Uri[] DownloadVideoUri;
    Uri[] DownloadImageUri;

    private StorageReference mVideoStorageRef, mImageStorageRef;

    Button mButtonChooseVideo, mButtonUpload, mButtonChooseImage, mButtonVisableDate;
    TextView mTextViewShowUploads;
    EditText mEditTextFileName;
    ProgressBar mProgressBar;
    VideoView mVideoView;
    CalendarView calendarView;
    ImageView mImageView;

    private CheckTypesTask checkTypesTask;


    public static VideoFragment newInstance() {
        VideoFragment fragment = new VideoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_video_fragment, container, false);

        mButtonVisableDate = view.findViewById(R.id.button_visable_date);
        mButtonChooseVideo = view.findViewById(R.id.button_choose_video);
        mButtonUpload = view.findViewById(R.id.button_upload);
        mVideoView = view.findViewById(R.id.video_view);
        mImageView = view.findViewById(R.id.image_view);
        mProgressBar = view.findViewById(R.id.progress_bar);
        mEditTextFileName = view.findViewById(R.id.edit_text_file_name);
        mButtonChooseImage = view.findViewById(R.id.button_choose_image);
        calendarView = view.findViewById(R.id.calendarView);
        mStorageRef = FirebaseStorage.getInstance().getReference("Uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("Uploads");


        mButtonVisableDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(calendarView.getVisibility()==View.VISIBLE){
                    calendarView.setVisibility(View.INVISIBLE);
                }else
                    calendarView.setVisibility(View.VISIBLE);
            }
        });



        //캘린더 뷰 날짜 받아오기
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( CalendarView view, int year, int month, int dayOfMonth) {
                String mMonth, mday;
                mMonth = String.format("%02d",month+1);
                mday = String.format("%02d",dayOfMonth);
                date = year+mMonth+mday;
                Toast.makeText(getActivity(), "날짜 : "+date, Toast.LENGTH_SHORT).show();
            }
        });
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
                    Toast.makeText(getContext(), "이미지가 업로드 중입니다", Toast.LENGTH_SHORT).show();
                } else {
                    Filename = mEditTextFileName.getText().toString().trim();
                    if (Filename == null) {
                        Filename = "No name";
                    } else {

                        //video 업로드 후 성공일 때 이미지 & 데이터베이스 등록
                        uploadVideoFile();

                        Log.d(TAG, "falisefalie");


                        Toast.makeText(getContext(), "이미지 업로드 성공", Toast.LENGTH_SHORT).show();


                    }
                }
            }
        });

        return view;

    }

    private void openFileImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
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
                        UploadImage uploadImage = new UploadImage(Filename, downloadUri.toString());
                        mDatabaseRef.child(Filename + "/(image)").setValue(uploadImage);
                        Log.d(TAG, "ImageUri : " + downloadUri.toString());
                        Toast.makeText(getActivity(), "업로드 성공", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getContext(), "업로드 실패 " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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
                        checkTypesTask = new CheckTypesTask(getActivity());
                        Toast.makeText(getContext(), "비디오 업로드 성공", Toast.LENGTH_SHORT).show();
                        uploadImageFile();
                        UploadVideo uploadVideo = new UploadVideo(Filename, downloadVideoUri.toString(),date);
                        mDatabaseRef.child(Filename + "/(video)").setValue(uploadVideo);
                        Log.d(TAG, "VideoUri : " + downloadVideoUri.toString());

                    } else {
                        Toast.makeText(getContext(), "비디오 업로드 실패 " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
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
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
                            Picasso.with(getContext()).load(mImageUri).into(mImageView);
                        }

                }

        }

    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
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
        Intent intent = new Intent(getContext(), VideosActivity.class);
        startActivity(intent);
    }

}
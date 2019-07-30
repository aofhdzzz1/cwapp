package com.example.uploadvideoapp;

import android.content.Intent;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import static android.content.ContentValues.TAG;

public class ImageFragment extends Fragment {

    private Button adSeletBtn, sendBtn;
    private EditText adNameText;
    private ImageView imageView;
    String Filename;

    private StorageReference mADStorageRef;
    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private int PICK_AD_REQUEST = 3;
    private StorageTask mUploadTask;
    private Uri mAdUri;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_image_fragment,container,false);

        adSeletBtn = (Button)view.findViewById(R.id.button_choose_image);
        sendBtn = (Button)view.findViewById(R.id.button_upload);
        adNameText = (EditText)view.findViewById(R.id.edit_text_file_name);
        imageView = (ImageView)view.findViewById(R.id.image_view);

        mStorageRef = FirebaseStorage.getInstance().getReference("AD");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("AD");

        adSeletBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChoser();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUploadTask != null && mUploadTask.isInProgress()) {
                    Toast.makeText(getContext(), "이미지가 업로드 되고 있습니다", Toast.LENGTH_SHORT).show();
                } else {
                    Filename = adNameText.getText().toString().trim();
                    if (Filename == null) {
                        adNameText.setText("no name");
                    } else {
                    upLoadAd();

                        Log.d(TAG, "falisefalie");


                    }
                }
            }
        });

        return view;
    }

    //이미지 창에서 선택하면 그 내용 이미지 뷰에 띄우기
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
               if(requestCode == PICK_AD_REQUEST)
                        if (data.getData() != null) {
                            mAdUri = data.getData();
                            Log.d(TAG, "mImageUri: " + mAdUri);
                            Picasso.with(getContext()).load(mAdUri).into(imageView);


                }

        }


//광고 업로드(Firebase database, storage)
    private void upLoadAd() {
            if(mAdUri!=null)
            mADStorageRef = mStorageRef.child(Filename);
            mADStorageRef.putFile(mAdUri).continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return mADStorageRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downLoadUri = task.getResult();

                        UploadAD uploadAD = new UploadAD(Filename,downLoadUri.toString());
                        mDatabaseRef.child(Filename).setValue(uploadAD);
                        Toast.makeText(getActivity(), "업로드 성공", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(getActivity(), "업로드 실패", Toast.LENGTH_SHORT).show();
                    }
                }
            });


    }

    //파일에서 이미지 선택하기 창 열기
    private void openFileChoser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_AD_REQUEST);
    }
}

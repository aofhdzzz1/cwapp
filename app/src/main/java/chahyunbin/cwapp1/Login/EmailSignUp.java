package chahyunbin.cwapp1.Login;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import chahyunbin.cwapp1.R;

public class EmailSignUp extends BaseActivity implements View.OnClickListener {

    EditText userConfirmPassword, userEmail, userPassword;
    ProgressBar progressBar;
    ImageView userImage;

    private FirebaseAuth mAuth;

    static int PReqCode = 1;
    static int REQUESTCODE = 1;
    Uri pickedImgUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_up);
        userEmail = (EditText) findViewById(R.id.fieldEmailSignUp);
        userPassword = (EditText) findViewById(R.id.fieldPasswordSignUp);
        userConfirmPassword = (EditText) findViewById(R.id.fieldConfirmPassword);





        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.emailSignUpButton).setOnClickListener(this);
        findViewById(R.id.textViewSignIn).setOnClickListener(this);

         progressBar = (ProgressBar)findViewById(R.id.progressbar);





    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK && requestCode ==REQUESTCODE && data != null){
            // the user has successfully picked an image
            // we need to save its reference to a Uri variable
            pickedImgUri = data.getData();
            userImage.setImageURI(pickedImgUri);
        }
    }


    private void registerUser(){
        String userconfirmpassword = userConfirmPassword.getText().toString().trim();
        String useremail = userEmail.getText().toString().trim();
        String userpassword = userPassword.getText().toString().trim();

        if(useremail.isEmpty()){
            userEmail.setError("Email is required");
            userEmail.requestFocus();
            return;
        }
        if(userpassword.isEmpty()){
           userPassword.setError("Password is required");
           userPassword.requestFocus();
        }
        if(userconfirmpassword.isEmpty()){
            userConfirmPassword.setError("Name is required");
            userConfirmPassword.requestFocus();
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()){
            userEmail.setError("Please enter a valid Email");
            userEmail.requestFocus();
            return;
        }
        if(userpassword.length()<6){
            userPassword.setError("Minimum length of password should be 6");
        }
        if(userpassword != userconfirmpassword){
            userConfirmPassword.setError("The password is not equal as the confirm password");
        }

        progressBar.setVisibility( View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(useremail,userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent intent = new Intent(EmailSignUp.this, EmailSignIn.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else
                    if(task.getException() instanceof FirebaseAuthUserCollisionException){
                        Toast.makeText(EmailSignUp.this, "you are already exception", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(EmailSignUp.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){

            case R.id.emailSignUpButton :
                registerUser();
                break;

            case R.id.textViewSignIn:
                Intent intent =  new Intent(EmailSignUp.this, EmailSignIn.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                break;

        }
    }
}

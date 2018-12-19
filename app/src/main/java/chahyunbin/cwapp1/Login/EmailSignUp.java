package chahyunbin.cwapp1.Login;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;

import chahyunbin.cwapp1.R;

public class EmailSignUp extends BaseActivity implements View.OnClickListener {

    EditText userName, userEmail, userPassword;
    ProgressBar progressBar;

    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_up);
        userName = (EditText) findViewById(R.id.fieldUserNameSignUp);
        userEmail = (EditText) findViewById(R.id.fieldEmailSignUp);
        userPassword = (EditText) findViewById(R.id.fieldPasswordSignUp);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.emailSignUpButton).setOnClickListener(this);
        findViewById(R.id.textViewSignIn).setOnClickListener(this);

         progressBar = (ProgressBar)findViewById(R.id.progressbar);

    }


    private void registerUser(){
        String username = userName.getText().toString().trim();
        String useremail = userEmail.getText().toString().trim();
        String userpassword = userPassword.getText().toString().trim();

        if(username.isEmpty()){
            userName.setError("Name is required");
            userName.requestFocus();
        }
        if(useremail.isEmpty()){
            userEmail.setError("Email is required");
            userEmail.requestFocus();
            return;
        }
        if(userpassword.isEmpty()){
           userPassword.setError("Password is required");
           userPassword.requestFocus();
        }
        if(username.isEmpty()){
            userName.setError("Name is required");
            userName.requestFocus();
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()){
            userEmail.setError("Please enter a valid Email");
            userEmail.requestFocus();
            return;
        }
        if(userpassword.length()<6){
            userPassword.setError("Minimum length of password should be 6");
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

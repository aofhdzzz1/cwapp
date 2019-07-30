package chahyunbin.cwapp1.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;

import chahyunbin.cwapp1.LodingActivity;
import chahyunbin.cwapp1.R;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class EmailSignInActivity extends BaseActivity implements View.OnClickListener {

    protected String TAG = "EmailSignInActivity";

    EditText userEmail, userPassword;
    public static String useremail;
    FirebaseAuth mAuth;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_sign_in);

        mAuth = FirebaseAuth.getInstance();
        progressBar = (ProgressBar) findViewById(R.id.progressbarSignin);
        userEmail = (EditText) findViewById(R.id.fieldEmail);
        userPassword = (EditText) findViewById(R.id.fieldPassword);

        findViewById(R.id.textViewSignUp).setOnClickListener(this);
        findViewById(R.id.emailSignInButton).setOnClickListener(this);
        findViewById(R.id.gohome).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.textViewSignUp :
                Intent intent = new Intent(EmailSignInActivity.this, EmailSignUpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                break;

            case R.id.emailSignInButton :
                userLogin();
                break;

            case R.id.gohome :
                Intent intent2 = new Intent(EmailSignInActivity.this, LoginHomeActivity.class);
                intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent2);
                break;

        }

    }

    private void userLogin() {

        useremail = userEmail.getText().toString().trim();
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
        if(!Patterns.EMAIL_ADDRESS.matcher(useremail).matches()){
            userEmail.setError("Please enter a valid Email");
            userEmail.requestFocus();
            return;
        }
        if(userpassword.length()<6){
            userPassword.setError("Minimum length of password should be 6");
        }
        progressBar.setVisibility( View.VISIBLE);


        mAuth.signInWithEmailAndPassword(useremail,userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()){
                    Intent intent = new Intent(EmailSignInActivity.this, LodingActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }else{
                    Toast.makeText(EmailSignInActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}

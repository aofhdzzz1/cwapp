package com.example.chahyunbin.cwapp.Login;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.util.Log;

import android.widget.Toast;



import com.example.chahyunbin.cwapp.MainActivity;
import com.example.chahyunbin.cwapp.R;

import com.facebook.CallbackManager;

import com.facebook.login.widget.LoginButton;
import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Arrays;
import java.util.List;

public class Loginmenu extends Activity {

    public static GoogleSignInClient mGoogleSignInClient;
    private static final int RC_SIGN_IN =1;
    final String TAG = "login Activity";
  public static FirebaseAuth mAuth;
    public static Context mcontext;
    public static String userName;
    public static String phoneNumber;
    private CallbackManager mCallbackManager;
    LoginButton facebookloginbtn;
    public static String facebookUserName;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;
    private boolean mVerificationInProgress = false;
    // Choose authentication providers
    public FirebaseUser user;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_menu);

        Log.d(TAG, "on create username = " + userName);
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.PhoneBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build(),
                new AuthUI.IdpConfig.FacebookBuilder().build());



        // Create and launch sign-in intent
            startActivityForResult(
                    AuthUI.getInstance()
                            .createSignInIntentBuilder()
                            .setAvailableProviders(providers)
                            .setIsSmartLockEnabled(false,true)
                            .build(),
                    RC_SIGN_IN);



    }




    //        /*
//
//                    GOOGLE LOGIN
//
//         */
//
//        mAuth = FirebaseAuth.getInstance();
//
//
//        SignInButton googlebtn =(SignInButton)findViewById(R.id.googlelogin);
//        googlebtn.setSize(SignInButton.SIZE_WIDE);
//
//
//        // Configure Google Sign In
//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestIdToken(getString(R.string.default_web_client_id))
//                .requestEmail()
//                .build();
//
//        // Build a GoogleSignInClient with the options specified by gso.
//        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
//
//        googlebtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public  void onClick(View v) {
//                signIn();
//            }
//        });
//    /*
//
//                    FACEBOOK LOGIN
//
//     */
//
//        // Initialize Facebook Login button
//        mCallbackManager = CallbackManager.Factory.create();
//        facebookloginbtn = findViewById(R.id.facebooklogin);
//        facebookloginbtn.setReadPermissions("email", "public_profile");
//        facebookloginbtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                facebooksignIN();
//                FirebaseUser facebookUser =FirebaseAuth.getInstance().getCurrentUser();
//                facebookUserName= facebookUser.getDisplayName();
//                finish();
//                startActivity(new Intent(Loginmenu.this, MainActivity.class));
//            }
//        });
//
//
//
//
//    }
//
//    private void handleFacebookAccessToken(AccessToken token) {
//        Log.d(TAG, "handleFacebookAccessToken:" + token);
//
//        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
////                            FirebaseUser user = mAuth.getCurrentUser();
////                            updateUI(user);
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(Loginmenu.this, "Authentication failed.",
//                                    Toast.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//
//                        // ...
//                    }
//                });
//    }
//
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        // Check if user is signed in (non-null) and update UI accordingly.
//        FirebaseUser currentUser = mAuth.getCurrentUser();
//        //updateUI(currentUser);
//        Toast.makeText(this, "already loggedin", Toast.LENGTH_SHORT).show();
//        if(currentUser !=null) {
//           userName = currentUser.getDisplayName();
//
//
//
//        }
//
//    }
//    //google signin()
//    private void signIn() {
//        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }
//    //facebook signin()
//    private void facebooksignIN(){
//        facebookloginbtn.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                Log.d(TAG, "facebook:onSuccess:" + loginResult);
//                handleFacebookAccessToken(loginResult.getAccessToken());
//
//            }
//
//            @Override
//            public void onCancel() {
//                Log.d(TAG, "facebook:onCancel");
//                // ...
//            }
//
//            @Override
//            public void onError(FacebookException error) {
//                Log.d(TAG, "facebook:onError", error);
//                // ...
//            }
//        });
//
//    }
//
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//            try {
//                // Google Sign In was successful, authenticate with Firebase
//                GoogleSignInAccount account = task.getResult(ApiException.class);
//                firebaseAuthWithGoogle(account);
//            } catch (ApiException e) {
//                // Google Sign In failed, update UI appropriately
//                Log.w(TAG, "Google sign in failed", e);
//                // ...
//            }
//        }
//    }
//    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
//        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
//
//        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
//        mAuth.signInWithCredential(credential)
//                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        if (task.isSuccessful()) {
//                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//                            finish();
//                            startActivity(new Intent(Loginmenu.this, MainActivity.class));
//                            //updateUI(user);
//
//                        } else {
//                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Toast.makeText(mcontext, "Authentication failed", Toast.LENGTH_SHORT).show();
//                            //updateUI(null);
//                        }
//
//
//                    }
//                });
//    }









//    @Override
//    protected void onStart() {
//
//        super.onStart();
//FirebaseAuth.getInstance().signOut();
//        List<AuthUI.IdpConfig> providers = Arrays.asList(
//                new AuthUI.IdpConfig.EmailBuilder().build(),
//                new AuthUI.IdpConfig.PhoneBuilder().build(),
//                new AuthUI.IdpConfig.GoogleBuilder().build(),
//                new AuthUI.IdpConfig.FacebookBuilder().build());
//
//        startActivityForResult(
//                AuthUI.getInstance()
//                        .createSignInIntentBuilder()
//                        .setAvailableProviders(providers)
//                        .build(),
//                RC_SIGN_IN);
//


    @Override
        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
            super.onActivityResult(requestCode, resultCode, data);


            if (requestCode == RC_SIGN_IN) {
                IdpResponse response = IdpResponse.fromResultIntent(data);

                if (resultCode == RESULT_OK) {
                    // Successfully signed in
                   user = FirebaseAuth.getInstance().getCurrentUser();
                   userName = user.getEmail();
                   phoneNumber = user.getPhoneNumber();
                   finish();
                   startActivity(new Intent(Loginmenu.this, MainActivity.class));
                } else {
                    Toast.makeText(mcontext, "signed fail", Toast.LENGTH_SHORT).show();
                }
            }
        }







}

package com.sani.shaheed.mymedicalproject;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import static com.sani.shaheed.mymedicalproject.MedList.RC_SIGN_IN;
import static com.sani.shaheed.mymedicalproject.SignUp.errorMsg;

public class SignIn extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private EditText sEmail, sPassword;
    private Button sButton, signUp_in_button;
    private GoogleApiClient mGoogleApiClient;
    private GoogleSignInOptions gso;
    private ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    public static final String TAG = "MEDICATION LIST";
    public static final String u_id = "U_ID";
    public static final String u_email = "U_EMAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        sEmail = findViewById(R.id.emailAddressEdt_signIn);
        sPassword = findViewById(R.id.passwordEdt_signIn);
        progressBar = findViewById(R.id.progress_bar);

        sButton = findViewById(R.id.sign_in_button);
        signUp_in_button = findViewById(R.id.sign_up_in_button);

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = sEmail.getText().toString().trim();
                String pass = sPassword.getText().toString().trim();

                if (email.isEmpty() || pass.isEmpty()){
                    fieldErrorMessage();
                }else {
                    signMeIn(email, pass);
                }
            }
        });

        signUp_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });
    }

    private void signMeIn(String email, String password) {
        progressBar.setVisibility(View.VISIBLE);
        sEmail.setEnabled(false);
        sPassword.setEnabled(false);
        sButton.setEnabled(false);
        signUp_in_button.setEnabled(false);
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                progressBar.setVisibility(View.GONE);
                                user = mAuth.getCurrentUser();
                                updateUI(user);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                Toast.makeText(getApplicationContext(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                Intent i = new Intent(getApplicationContext(), SignUp.class);
                                startActivity(i);
                                finish();
                            }
                        }
                    });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()){
                GoogleSignInAccount account = result.getSignInAccount();
                if (account != null){
                    firebaseAuthWithGoogle(account);
                }
            }else {
                Log.d(TAG, "onActivityResult: " + result.isSuccess());
            }
        }
    }

    private void signInwithGoogle(){
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        mAuth = FirebaseAuth.getInstance();

        AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(authCredential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user = mAuth.getCurrentUser();
                            if (user != null){
                                updateUI(user);
                            }
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null){
            Intent i = new Intent(this, MedList.class);
            i.putExtra(u_id, String.valueOf(user.getUid()));
            i.putExtra(u_email, String.valueOf(user.getEmail()));
            startActivity(i);
        }
    }

    private void fieldErrorMessage() {
        if (sEmail.getText() == null || sPassword.getText() == null){
            if (sEmail.getText() == null){
                sEmail.setError(errorMsg);
            }else if (sPassword.getText() == null){
                sPassword.setError(errorMsg);
            }
        }else if (sEmail.getText() == null && sPassword.getText() == null){
            sPassword.setError(errorMsg);
            sEmail.setError(errorMsg);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

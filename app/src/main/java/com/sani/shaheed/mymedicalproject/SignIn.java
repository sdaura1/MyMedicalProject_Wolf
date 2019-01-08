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
import android.widget.Toast;

//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.auth.api.signin.GoogleSignInResult;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.SignInButton;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.tasks.OnCompleteListener;
//import com.google.android.gms.tasks.Task;
//import com.google.firebase.auth.AuthCredential;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
//import com.google.firebase.auth.FirebaseUser;
//import com.google.firebase.auth.GoogleAuthProvider;

import static com.sani.shaheed.mymedicalproject.MedList.RC_SIGN_IN;

public class SignIn extends AppCompatActivity {

    private EditText sEmail, sPassword;
    private Button sButton, signUp_in_button;
    public static final String TAG = "MEDICATION LIST";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        sEmail = findViewById(R.id.emailAddressEdt_signIn);
        sPassword = findViewById(R.id.passwordEdt_signIn);

        sButton = findViewById(R.id.sign_in_button);
        signUp_in_button = findViewById(R.id.sign_up_in_button);

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = sEmail.getText().toString().trim();
                String pass = sPassword.getText().toString().trim();
                signMeIn(email, pass);
            }
        });

        signUp_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUp.class));
            }
        });
    }

    private void signMeIn(String username, String password){
        if (username.isEmpty() || password.isEmpty()){
            fieldErrorMessage();
        }else {
            startActivity(new Intent(this, MedList.class));
        }
    }

    private void fieldErrorMessage() {

        EditText email = findViewById(R.id.emailAddressEdt_signIn);
        EditText pass = findViewById(R.id.passwordEdt_signIn);

        if (email.getText() == null || pass.getText() == null){

            if (email.getText() == null){
                email.setError("This is needed!");
            }else if (pass.getText() == null){
                pass.setError("This is needed!");
            }
        }else if (email.getText() == null && pass.getText() == null){
            pass.setError("This is needed!");
            email.setError("This is needed!");
        }
    }
}

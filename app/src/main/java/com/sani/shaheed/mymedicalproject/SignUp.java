package com.sani.shaheed.mymedicalproject;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class SignUp extends AppCompatActivity {
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Button signup_button;
    private EditText email_signup, password_signup;
    private String email, password, userEmail, userUID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signup_button = findViewById(R.id.sign_up_button);
        email_signup = findViewById(R.id.emailAddressEdt_signUp);
        password_signup = findViewById(R.id.passwordEdt_signUp);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = email_signup.getText().toString();
                password = password_signup.getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    Toast.makeText(SignUp.this, "Everything is needed", Toast.LENGTH_LONG).show();
                }else {
                    signMeUp(email, password);
                }
            }
        });

    }

    private void updateUI(FirebaseUser user) {
        if (user != null){
            userEmail = user.getEmail();
            userUID = user.getUid();
        }
    }

    private void signMeUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user = mAuth.getCurrentUser();
                            if (user != null){
                                updateUI(user);
                            }
                        }else {
                            updateUI(null);
                        }
                    }
                });
    }
}

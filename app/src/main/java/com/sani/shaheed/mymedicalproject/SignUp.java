package com.sani.shaheed.mymedicalproject;

import android.content.Intent;
import android.graphics.Color;
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

import static com.sani.shaheed.mymedicalproject.SignIn.u_email;
import static com.sani.shaheed.mymedicalproject.SignIn.u_id;

public class SignUp extends AppCompatActivity {

    private FirebaseUser user;
    private FirebaseAuth mAuth;
    private Button signup_button;
    private EditText email_signup, password_signup;
    private String email, password;
    public static final String errorMsg = "Field is required";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        signup_button = findViewById(R.id.sign_up_button);
        email_signup = findViewById(R.id.emailAddressEdt_signUp);
        password_signup = findViewById(R.id.passwordEdt_signUp);

        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                email = email_signup.getText().toString();
                password = password_signup.getText().toString();

                if (email.isEmpty() || password.isEmpty()){
                    fieldErrorMessage();
                }else {
                    signMeUp(email, password);
                }
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null){
            Intent i = new Intent(this, MedList.class);
            i.putExtra(u_id, user.getUid());
            i.putExtra(u_email, user.getEmail());
            startActivity(i);
            finish();
        }
    }

    private void fieldErrorMessage() {
        if (email_signup.getText() == null || password_signup.getText() == null){
            if (email_signup.getText() == null){
                email_signup.setError(errorMsg);
            }else if (password_signup.getText() == null){
                password_signup.setError(errorMsg);
            }
        }else if (email_signup.getText() == null && password_signup.getText() == null){
            password_signup.setError(errorMsg);
            email_signup.setError(errorMsg);
        }
    }

    private void signMeUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            user = mAuth.getCurrentUser();
                                updateUI(user);
                        }else {
                            updateUI(null);
                        }
                    }
                });
    }
}

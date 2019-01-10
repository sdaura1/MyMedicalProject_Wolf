package com.sani.shaheed.mymedicalproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {
    private Button signup_button;
    private EditText email_signup, password_signup;
    private String email, password;

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
                signMeUp(email, password);
            }
        });
    }

    private void signMeUp(String username, String password){
        if (username.isEmpty() || password.isEmpty()){
            fieldErrorMessage();
        }else {
            startActivity(new Intent(SignUp.this, MedList.class));
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

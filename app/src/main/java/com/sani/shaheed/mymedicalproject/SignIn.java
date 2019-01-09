package com.sani.shaheed.mymedicalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignIn extends AppCompatActivity {

    private EditText sEmail, sPassword;
    private Button sButton, signUp_in_button;
    public static final String errorMsg = "Field is required";
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    public static final String TAG = "MEDICATION LIST";
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mAuth = FirebaseAuth.getInstance();

        sEmail = findViewById(R.id.emailAddressEdt_signIn);
        sPassword = findViewById(R.id.passwordEdt_signIn);
        progressBar = findViewById(R.id.progressBar);

        sButton = findViewById(R.id.sign_in_button);
        signUp_in_button = findViewById(R.id.sign_up_in_button);

        progressBar.setVisibility(View.GONE);

        sButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = sEmail.getText().toString().trim();
                String pass = sPassword.getText().toString().trim();

                if (email.isEmpty() || pass.isEmpty()) {
                    fieldErrorMessage();
                } else {
                    signMeIn(email, pass);
                    loginToServer(email, pass);
                }
            }
        });
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void fieldErrorMessage() {

        EditText email = findViewById(R.id.emailAddressEdt_signIn);
        EditText pass = findViewById(R.id.passwordEdt_signIn);

        if (email.getText() == null || pass.getText() == null){

            if (email.getText() == null){
                email.setError(errorMsg);
                requestFocus(email);
            }else if (pass.getText() == null){
                pass.setError(errorMsg);
                requestFocus(pass);
            }
        }
    }

    private void loginToServer(final String username, final String password) {
        APIServices services = APIClient.getClient().create(APIServices.class);

        Call<MSG> userCall = services.loginFormInfo(username, password);

        userCall.enqueue(new Callback<MSG>() {
            @Override
            public void onResponse(Call<MSG> call, Response<MSG> response) {

                if (response != null) {
                    if (!(response.body().getError())) {
                        Intent i = new Intent(SignIn.this, MedList.class);

                        i.putExtra("username", sEmail.getText().toString());
                        i.putExtra("password", password);
                        Toast.makeText(SignIn.this, "Successful", Toast.LENGTH_SHORT).show();

                        startActivity(i);
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<MSG> call, Throwable t) {
                Toast.makeText(SignIn.this, "Could not reach server because " + t.getMessage(),
                        Toast.LENGTH_SHORT).show();
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

                            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(SignIn.this, R.style.AlertDialogCustom)
                                    .setIcon(R.drawable.ic_warning_black_24dp)
                                    .setMessage("Authentication Failed! Please try again or Sign Up if you do not have an account")
                                    .setCancelable(true)
                                    .setTitle("Sign In Message");
                            AlertDialog dialog = dialogBuilder.create();
                            dialog.show();

                            sEmail.setEnabled(true);
                            sPassword.setEnabled(true);
                            sButton.setEnabled(true);
                            signUp_in_button.setEnabled(true);
                        }
                    }
                });
    }

    private void updateUI(FirebaseUser user) {
        Intent i = new Intent(this, MedList.class);
        i.putExtra("UserID", user.getUid());
        i.putExtra("UserEmail", user.getEmail());
        startActivity(i);
    }

    private void signMeUp(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            user = mAuth.getCurrentUser();
//                            updateUI(user);
                        } else {
//                            updateUI(null);
                        }
                    }
                });
    }
}

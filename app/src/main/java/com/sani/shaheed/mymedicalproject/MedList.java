package com.sani.shaheed.mymedicalproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.sani.shaheed.mymedicalproject.SignIn.u_id;

public class MedList extends AppCompatActivity  {

    public static final int RC_SIGN_IN = 9001;
    public static final String TAG = "MEDICATION LIST";
    private RecyclerView recordL;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private FloatingActionButton addButton;
    private String userId;
    private List<Medicine> medList;
    private Medicine medicine;
    private Intent i;
    private Medicine[] medicines;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        i = getIntent();
        userId = i.getStringExtra(u_id);

        databaseReference = FirebaseDatabase.getInstance().getReference("Medication/Users/" + userId);

        recordL = findViewById(R.id.recordL);
        addButton = findViewById(R.id.theaddBtn);
        medList = new ArrayList<>();

        setUpFirebaseAdapter();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = getIntent();

                if (i != null && i.hasExtra(u_id)){
                    userId = i.getStringExtra(userId);
                    Intent newIntent = new Intent(MedList.this, InsertActivity.class);
                    newIntent.putExtra(u_id, userId);
                    startActivity(newIntent);
                }
            }
        });

    }

    private void setUpFirebaseAdapter(){
        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Medicine, FirebaseViewHolder>(
                Medicine.class, R.layout.model_layout, FirebaseViewHolder.class, databaseReference
        ) {
            @Override
            protected void populateViewHolder(FirebaseViewHolder viewHolder, Medicine model, int position) {
                viewHolder.onBindViewHolder(model);
            }
        };

        recordL.setHasFixedSize(true);
        recordL.setLayoutManager(new LinearLayoutManager(this));
        recordL.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.cleanup();
    }
}
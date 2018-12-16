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

import java.util.ArrayList;
import java.util.List;

public class MedList extends AppCompatActivity  {

    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseUser user;
    private FirebaseAuth mAuth;
    public static final int RC_SIGN_IN = 9001;
    public static final String TAG = "MEDICATION LIST";
    private RecyclerView recordL;
    private FloatingActionButton addButton;
    private SQLiteHelper sqLiteHelper;
    private Cursor cursor;
    private List<Medicine> medList;
    private Adapter adapter;
    private Medicine medicine;
    private String userUID;
    private String userEmail;

//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth = FirebaseAuth.getInstance();
//        mAuth.addAuthStateListener(mAuthListener);
//
//        user = mAuth.getCurrentUser();
//        updateUI(user);
//
//    }
//
//    public void updateUI(FirebaseUser user){
//        userUID = user.getUid();
//        userEmail = user.getEmail();
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        getData();

    }

    private void getData() {

        recordL = findViewById(R.id.recordL);
        addButton = findViewById(R.id.theaddBtn);
        sqLiteHelper = new SQLiteHelper(this);
        medList = new ArrayList<>();

        cursor = sqLiteHelper.getAllEntries();

        if (cursor.moveToFirst()) {
            do {
                medicine = new Medicine();
                medicine.setId(cursor.getInt(cursor.getColumnIndex("_ID")));
                medicine.setName(cursor.getString(cursor.getColumnIndex("medName")));
                medicine.setDescription(cursor.getString(cursor.getColumnIndex("medDescription")));
                medicine.setDate(cursor.getString(cursor.getColumnIndex("entryDate")));
                medicine.setDosage(cursor.getInt(cursor.getColumnIndex("dosage")));

                medList.add(medicine);
            } while (cursor.moveToNext());
        }

        adapter = new Adapter(this, medList, new CustomItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent i = new Intent(MedList.this, InsertActivity.class);
                int id = medList.get(position).getId();
                i.putExtra(InsertActivity.EXTRA_IN, id);
                startActivity(i);
            }
        });

        DividerItemDecoration decoration = new DividerItemDecoration(this,
                new LinearLayoutManager(this).getOrientation());

        recordL.addItemDecoration(decoration);
        recordL.setHasFixedSize(true);
        recordL.setLayoutManager(new LinearLayoutManager(this));
        recordL.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MedList.this, InsertActivity.class));
            }
        });

        cursor.close();
        sqLiteHelper.close();
    }


    @Override
    protected void onResume() {
        super.onResume();

        getData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.signInMenu:
                Intent i = new Intent(this, SignIn.class);
                startActivity(i);

            case R.id.signOutMenu:
                FirebaseAuth.getInstance().signOut();
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//
//        if (mAuthListener != null){
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }
}
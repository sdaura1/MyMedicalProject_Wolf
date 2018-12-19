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
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Calendar;
import java.util.List;

import static com.sani.shaheed.mymedicalproject.SignIn.u_email;
import static com.sani.shaheed.mymedicalproject.SignIn.u_id;

public class MedList extends AppCompatActivity  {

    public static final int RC_SIGN_IN = 9001;
    public static final String TAG = "MEDICATION LIST";
    private RecyclerView recordL;
    private DatabaseReference databaseReference;
    private FirebaseRecyclerAdapter firebaseRecyclerAdapter;
    private FloatingActionButton addButton;
    private String userId;
    private Calendar calendar;
    private ArrayList<Medicine> medList;
    private Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        recordL = findViewById(R.id.recordL);
        addButton = findViewById(R.id.theaddBtn);
        medList = new ArrayList<>();
        calendar = Calendar.getInstance();

        i = getIntent();
        userId = i.getStringExtra(u_id);

        setUpFirebaseAdapter();

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (userId.isEmpty()){
                    Toast.makeText(MedList.this, "Error", Toast.LENGTH_SHORT).show();
                }else {
                    Intent newIntent = new Intent(MedList.this, InsertActivity.class);
                    newIntent.putExtra(u_id, userId);
                    startActivity(newIntent);
                }
            }
        });
    }

    private void setUpFirebaseAdapter(){

        databaseReference = FirebaseDatabase.getInstance().getReference("Medications/Users/" + userId);
        databaseReference.keepSynced(true);

        firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<Medicine, FirebaseViewHolder>(
                Medicine.class, R.layout.model_layout, FirebaseViewHolder.class, databaseReference
        ) {
            @Override
            protected void populateViewHolder(final FirebaseViewHolder viewHolder, Medicine model, int position) {
                viewHolder.setAdapterDescription(model.getDescription());
                viewHolder.setAdapterName(model.getName());
                viewHolder.setLetterText(model.getName());
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getApplicationContext(), InsertActivity.class);
                        startActivity(i);
                    }
                });
            }
        };

        recordL.setHasFixedSize(true);
        recordL.setLayoutManager(new LinearLayoutManager(this));
        recordL.setAdapter(firebaseRecyclerAdapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        setUpFirebaseAdapter();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpFirebaseAdapter();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseRecyclerAdapter.cleanup();
    }

    public static class FirebaseViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView medName, medDescription, theLetterText;

        public FirebaseViewHolder(View itemView){
            super(itemView);
            mView = itemView;
        }

        public void setAdapterName(String mName){
            medName = mView.findViewById(R.id.medicationName);
            medName.setText(mName);
        }

        public void setAdapterDescription(String mDesc){
            medDescription = mView.findViewById(R.id.medicationDescription);
            medDescription.setText(mDesc);
        }

        public void setLetterText(String letterText){
            theLetterText = mView.findViewById(R.id.theLetterText);
            theLetterText.setText(String.valueOf(letterText.charAt(0)).toUpperCase());
        }
    }

}

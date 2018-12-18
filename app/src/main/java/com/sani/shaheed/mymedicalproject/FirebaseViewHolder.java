package com.sani.shaheed.mymedicalproject;

import android.content.Context;
import android.content.Intent;
import android.provider.Contacts;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

class FirebaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private Context context;
    private View mView;
    private ArrayList<Medicine> medList;
    private DatabaseReference databaseReference;

    public FirebaseViewHolder(View itemView){
        super(itemView);
        mView = itemView;
        context = itemView.getContext();
        itemView.setOnClickListener(this);
    }

    public void onBindViewHolder(Medicine medicine) {
        TextView medName, medDescription, theLetterText;

        medName = mView.findViewById(R.id.medicationName);
        medDescription = mView.findViewById(R.id.medicationDescription);
        theLetterText = mView.findViewById(R.id.theLetterText);

        medName.setText(medicine.getName());
        medDescription.setText(medicine.getDescription());
        theLetterText.setText(String.valueOf(medicine.getName().charAt(0)).toUpperCase());

    }

    @Override
    public void onClick(View v) {
        medList = new ArrayList<>();

        databaseReference = FirebaseDatabase.getInstance().getReference("Medication/Users");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    medList.add(snapshot.getValue(Medicine.class));
                }

                int itemPosition = getLayoutPosition();

                Intent i = new Intent(context, InsertActivity.class);
                i.putExtra("Position", itemPosition);
                context.startActivity(i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}


package com.sani.shaheed.mymedicalproject;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class RecordList extends AppCompatActivity {

    private static final String TAG = "TEST";
    private FloatingActionButton addBtn;
    private Adapter adapter;
    private List<Medicine> medList;
    private Cursor cursor;
    private SQLiteHelper sqLiteHelper;
    private RecyclerView recyclerView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        recyclerView = findViewById(R.id.recordListView);
        addBtn = findViewById(R.id.addBtn);
        medList = new ArrayList<>();

        sqLiteHelper = new SQLiteHelper(this);

        cursor = sqLiteHelper.getAllEntries();

        getValues(cursor);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                sqLiteHelper.deleteEntryById(position);
            }
        }).attachToRecyclerView(recyclerView);

        DividerItemDecoration decoration = new DividerItemDecoration(this, new LinearLayoutManager(RecordList.this).getOrientation());

        recyclerView.addItemDecoration(decoration);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RecordList.this, InsertActivity.class));
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

//        sqLiteHelper = new SQLiteHelper(this);
//
//        cursor = sqLiteHelper.getAllEntries();
//        getValues(cursor);
    }

    public void getValues(Cursor cursor) {

        if(cursor.moveToFirst()) {
            do {
                Medicine med = new Medicine();
                med.setName(cursor.getString(cursor.getColumnIndex("medName")));
                med.setDescription(cursor.getString(cursor.getColumnIndex("medDescription")));
                med.setDate(cursor.getString(cursor.getColumnIndex("entryDate")));

                medList.add(med);
            }while(cursor.moveToNext());
        }

        adapter = new Adapter(this, medList, new CustomItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.d(TAG, "onItemClick: " + position);
                Intent i = new Intent(RecordList.this, InsertActivity.class);
                i.putExtra(InsertActivity.EXTRA_IN, position + 1);
                startActivity(i);
            }
        });

        recyclerView.setAdapter(adapter);
        sqLiteHelper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        sqLiteHelper = new SQLiteHelper(this);
//
//        cursor = sqLiteHelper.getAllEntries();
//        getValues(cursor);
    }
}

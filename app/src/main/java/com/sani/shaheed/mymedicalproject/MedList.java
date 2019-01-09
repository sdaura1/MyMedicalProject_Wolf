package com.sani.shaheed.mymedicalproject;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MedList extends AppCompatActivity  {

    public static final int RC_SIGN_IN = 9001;
    public static final String TAG = "MEDICATION LIST";
    private RecyclerView recordL;
    private FloatingActionButton addButton;
    private SQLiteHelper sqLiteHelper;
    private Cursor cursor;
    private List<Medicine> medList;
    private Intent alarmIntent;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private Adapter adapter;
    private Medicine medicine;
    private Intent incomingIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        recordL = findViewById(R.id.recordL);
        incomingIntent = getIntent();

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                int id = medList.get(position).getId();
                Log.d(TAG, "onSwiped: " + id);
                boolean isDeleted = sqLiteHelper.deleteEntryById(id);
                if (isDeleted){
                    cancelAlarm(id);
                    getData();
                }else {
                    Toast.makeText(MedList.this, "It cannot be deleted!", Toast.LENGTH_SHORT).show();
                }
            }
        }).attachToRecyclerView(recordL);

        getData();
    }

    private void cancelAlarm(int alarmId){

        alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), alarmId, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        if (alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }

        Toast.makeText(getApplicationContext(),
                "Alarm Deleted", Toast.LENGTH_SHORT).show();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if (incomingIntent != null) {
            menu.findItem(R.id.sync).setVisible(false);
            menu.findItem(R.id.push).setVisible(true);
        } else {
            menu.findItem(R.id.push).setVisible(false);
            menu.findItem(R.id.sync).setVisible(true);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.sync:
                startActivity(new Intent(this, SignIn.class));
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        getData();
    }
}
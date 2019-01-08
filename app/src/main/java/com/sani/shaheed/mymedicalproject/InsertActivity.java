package com.sani.shaheed.mymedicalproject;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class InsertActivity extends AppCompatActivity {

    public static final String EXTRA_IN = "UPDATE_ID";
    public static final String ALARM_EXTRA = "ALARM_EXTRA";
    private SQLiteHelper sqLiteHelper;
    private FloatingActionButton saveButton;
    private EditText medNameEditText, medDescriptionEditText, medIntervalEditText, medDosageEditText;
    private ImageView alarmBell, deleteBin;
    private TextView entryDateTextView, medicationInfoTextView, medDosageTextView;
    private String mName, mDes, mInt,  mEntry, mDosage;
    private Cursor cursor;
    private Intent alarmIntent;
    private Intent intent;
    private Ringtone ringtone = null;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private SwitchCompat mySwitch;
    private int position, alarmID, alarmOnOff;
    private boolean deleted;
    private long inserted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        sqLiteHelper = new SQLiteHelper(this);

        medNameEditText = findViewById(R.id.med_name_id);
        medDescriptionEditText = findViewById(R.id.med_description_id);
        medIntervalEditText = findViewById(R.id.med_interval_id);
        medDosageEditText = findViewById(R.id.med_dosage);
        medDosageTextView = findViewById(R.id.medDosageTextView);
        saveButton = findViewById(R.id.save_id);
        entryDateTextView = findViewById(R.id.med_entryDate);
        alarmBell = findViewById(R.id.alarmingId);
        medicationInfoTextView = findViewById(R.id.medicationInfo);
        mySwitch = findViewById(R.id.alarmSwitch);
        deleteBin = findViewById(R.id.deleteBin);

        intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_IN)){
            saveButton.setBackgroundResource(R.drawable.ic_update_black_24dp);
            position = intent.getIntExtra(EXTRA_IN, position);
            alarmBell.setVisibility(View.GONE);
            medDosageTextView.setVisibility(View.GONE);

            cursor = sqLiteHelper.getEntryById(position);

                if (cursor.moveToFirst() && cursor != null) {
                    do {
                        alarmID = Integer.valueOf(cursor.getString(cursor.getColumnIndex("_ID")));
                        mName = cursor.getString(cursor.getColumnIndex("medName"));
                        mDes = cursor.getString(cursor.getColumnIndex("medDescription"));
                        mInt = cursor.getString(cursor.getColumnIndex("medInterval"));
                        mEntry = cursor.getString(cursor.getColumnIndex("entryDate"));
                        mDosage = cursor.getString(cursor.getColumnIndex("dosage"));
                        alarmOnOff = Integer.valueOf(cursor.getString(cursor.getColumnIndex("alarmOnOff")));
                    } while (cursor.moveToNext());

                    medNameEditText.setText(mName);
                    medDescriptionEditText.setText(mDes);
                    medIntervalEditText.setText(mInt);
                    entryDateTextView.setText(mEntry);
                    medDosageEditText.setText(mDosage);
                    if (alarmOnOff == 0){
                        mySwitch.setChecked(false);
                    }else {
                        mySwitch.setChecked(true);
                    }

                    cursor.close();
                }

            deleteBin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Snackbar.make(getCurrentFocus(), "Are you sure you want to delete ?", Snackbar.LENGTH_LONG)
                            .setAction("YES", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    deleted = sqLiteHelper.deleteEntryById(position);
                                    if (deleted){
                                        cancelAlarm(position);
                                        finish();
                                    }else {
                                        Toast.makeText(getApplicationContext(),
                                                "Error in deleting!", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }).show();
                }
            });


            mySwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked){
                        alarmOnOff = 1;
                    }else {
                        alarmOnOff = 0;
                    }
                }
            });

            saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        if (medNameEditText.getText().toString().equals("")
                                || medDescriptionEditText.getText().toString().equals("")
                                || entryDateTextView.getText().toString().equals("")
                                || medIntervalEditText.getText().toString().equals("")
                                || medDosageEditText.getText().toString().equals("")){

                            Toast.makeText(getApplicationContext(), "Everything is needed!",
                                    Toast.LENGTH_SHORT).show();

                        }else {

                            String updateName, updateDes, updateEntry;
                            int updateInterval, updateDosage, updateAlarmOnOff;

                            updateName = medNameEditText.getText().toString();
                            updateDes = medDescriptionEditText.getText().toString();
                            updateEntry = entryDateTextView.getText().toString();
                            updateInterval = Integer.valueOf(medIntervalEditText.getText().toString());
                            updateDosage = Integer.valueOf(medDosageEditText.getText().toString());
                            updateAlarmOnOff = alarmOnOff;

                            long i = sqLiteHelper.updateEntry(position, updateName, updateDes, updateInterval, updateEntry,updateDosage, updateAlarmOnOff);

                            if (updateAlarmOnOff == 0){
                                cancelAlarm(position);
                                finish();
                            }else {
                                if (i > 0){
                                    setAlarm(updateInterval, position);
                                    finish();
                                }else {
                                    Toast.makeText(getApplicationContext(),
                                            "Error in update", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
        }else if (intent != null && intent.hasExtra(ALARM_EXTRA)){

            position = intent.getIntExtra(ALARM_EXTRA, position);
            deleteBin.setVisibility(View.GONE);
            mySwitch.setVisibility(View.GONE);
            medIntervalEditText.setVisibility(View.GONE);
            medDescriptionEditText.setVisibility(View.GONE);
            medNameEditText.setVisibility(View.GONE);
            medDosageEditText.setVisibility(View.GONE);

            cursor = sqLiteHelper.getEntryById(position);

            if (cursor.moveToFirst() && cursor != null){
                do {
                    mName = cursor.getString(cursor.getColumnIndex("medName"));
                    mDosage = cursor.getString(cursor.getColumnIndex("dosage"));
                }while (cursor.moveToNext());
            }

            medDosageTextView.setText("Dosage: " + mDosage);
            medicationInfoTextView.setText(mName);
            saveButton.setBackgroundResource(R.drawable.ic_close_black_24dp);

            final Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);

            alarmBell.setImageResource(R.drawable.ic_alarm_black_24dp);

            if (cursor != null && ringtone != null && ringtone.isPlaying()){
                ringtone.stop();
                ringtone = RingtoneManager.getRingtone(this, uri);
                ringtone.play();
            }else {
                ringtone = RingtoneManager.getRingtone(this, uri);
                ringtone.play();
            }

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (ringtone != null){
                        ringtone.stop();
                        finish();
                    }
                }
            });

        }else {

            alarmBell.setVisibility(View.GONE);
            medDosageTextView.setVisibility(View.GONE);
            position = 0;
            deleteBin.setVisibility(View.GONE);
            mySwitch.setVisibility(View.GONE);

            entryDateTextView.setText(new SimpleDateFormat("d - MMMM - yyyy", Locale.US)
                    .format(Calendar.getInstance().getTime()));

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (medNameEditText.getText().toString().equals("")
                            || medDescriptionEditText.getText().toString().equals("")
                            || entryDateTextView.getText().toString().equals("")
                            || medIntervalEditText.getText().toString().equals("")
                            || medDosageEditText.getText().toString().equals("")){

                        Toast.makeText(InsertActivity.this,
                                "Everything is required!", Toast.LENGTH_SHORT).show();

                    }else {

                        String newName, newDescription, newEntrydate;
                        int newInterval, newDosage;

                        newName= medNameEditText.getText().toString();
                        newDescription = medDescriptionEditText.getText().toString();
                        newEntrydate = entryDateTextView.getText().toString();
                        newInterval = Integer.valueOf(medIntervalEditText.getText().toString());
                        newDosage = Integer.valueOf(medDosageEditText.getText().toString());
                        alarmOnOff = 1;
                        saveButton.setBackgroundResource(R.drawable.ic_check_black_24dp);

                        inserted = sqLiteHelper.insert(newName, newDescription, newInterval, newEntrydate, newDosage, alarmOnOff);

                        if (inserted > 0){

                            cursor = sqLiteHelper.getEntryById((int) inserted);

                            if (cursor.moveToFirst() && cursor != null){
                                do {
                                    alarmID = Integer.valueOf(cursor.getString(cursor.getColumnIndex("_ID")));
                                }while (cursor.moveToNext());
                            }

                            setAlarm(newInterval, alarmID);
                            finish();

                        }else {
                            Toast.makeText(getApplicationContext(),
                                    "Error in saving!", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            });
        }
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

    private void setAlarm(int alarminterval, int medAlarmId){

        int interval = alarminterval * 3600000;

        alarmIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        alarmIntent.putExtra(ALARM_EXTRA, medAlarmId);
        pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), medAlarmId, alarmIntent, 0);
        alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + interval,
                interval, pendingIntent);

        Toast.makeText(getApplicationContext(),
                "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        final Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, uri);

        if (ringtone != null){
            ringtone.stop();
            finish();
        }
    }
}

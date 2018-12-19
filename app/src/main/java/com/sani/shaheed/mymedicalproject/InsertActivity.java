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
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import static com.sani.shaheed.mymedicalproject.SignIn.u_id;

public class InsertActivity extends AppCompatActivity {

    public static final String EXTRA_IN = "UPDATE_ID";
    public static final String ALARM_EXTRA = "ALARM_EXTRA";
    private SQLiteHelper sqLiteHelper;
    private Button saveButton;
    private EditText medNameEditText, medDescriptionEditText, medIntervalEditText, medDosageEditText;
    private ImageView alarmBell, deleteBin;
    private TextView entryDateTextView, medicationInfoTextView, medDosageTextView;
    private String mName, mDes, mInt,  mEntry, mDosage, userId, the_date;
    private String dataId;
    private Calendar calendar;
    private Intent alarmIntent;
    private Intent intent;
    private Ringtone ringtone = null;
    private FirebaseUser user;
    private DatabaseReference databaseReference;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;
    private SwitchCompat mySwitch;
    private Map<String, Object> savingMap;
    private Date today;
    private SimpleDateFormat simpleDateFormat;
    private int position, alarmID, alarmOnOff;
    private boolean deleted;
    private long inserted;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(this, MedList.class);
        i.putExtra(u_id, userId);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        sqLiteHelper = new SQLiteHelper(this);
        databaseReference = FirebaseDatabase.getInstance().getReference("Medications/Users");

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
        savingMap = new HashMap<>();
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.US);
        the_date = simpleDateFormat.format(calendar.getTime());


        intent = getIntent();

        if (intent != null && intent.hasExtra(EXTRA_IN)){
            saveButton.setText(R.string.update);
            position = intent.getIntExtra(EXTRA_IN, position);
            alarmBell.setVisibility(View.GONE);
            medDosageTextView.setVisibility(View.GONE);


        }else if (intent != null && intent.hasExtra(ALARM_EXTRA)){

            position = intent.getIntExtra(ALARM_EXTRA, position);
            deleteBin.setVisibility(View.GONE);
            mySwitch.setVisibility(View.GONE);

        }else if (intent != null && intent.hasExtra(u_id)){

            intent = getIntent();
            userId = intent.getStringExtra(u_id);

            alarmBell.setVisibility(View.GONE);
            medDosageTextView.setVisibility(View.GONE);
            entryDateTextView.setText(the_date);
            deleteBin.setVisibility(View.GONE);
            mySwitch.setVisibility(View.GONE);

            saveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDes = medDescriptionEditText.getText().toString().trim();
                    mDosage = medDosageEditText.getText().toString().trim();
                    mEntry = entryDateTextView.getText().toString().trim();
                    mName = medNameEditText.getText().toString().trim();
                    mInt = medIntervalEditText.getText().toString().trim();

                    if (mInt.isEmpty() || mName.isEmpty() || mEntry.isEmpty() || mDosage.isEmpty() || mDes.isEmpty()){

                        Toast.makeText(InsertActivity.this, "Everything is needed", Toast.LENGTH_LONG).show();

                    }else {

                        Medicine medicine = new Medicine();

                        medicine.setDate(mEntry);
                        medicine.setDescription(mDes);
                        medicine.setDosage(Integer.valueOf(mDosage));
                        medicine.setInterval(Integer.valueOf(mInt));
                        medicine.setName(mName);
                        medicine.setId(userId);

                        String key = databaseReference.child(userId).getKey();

                            databaseReference.child(key).child(mName).setValue(medicine).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent i = new Intent(getApplicationContext(), MedList.class);
                                        i.putExtra(u_id, userId);
                                        startActivity(i);
//                                        setAlarm(mInt, userId);
                                    }else {
                                        Toast.makeText(InsertActivity.this, "Error", Toast.LENGTH_SHORT).show();
                                        Log.d(MedList.TAG, "onComplete: " + task.getException());
                                    }
                                }
                            });
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

    private void setUpFirebaseAdapter(){

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

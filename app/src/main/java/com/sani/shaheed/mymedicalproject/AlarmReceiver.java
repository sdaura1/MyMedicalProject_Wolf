package com.sani.shaheed.mymedicalproject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;

import com.sani.shaheed.mymedicalproject.activities.InsertActivity;

public class AlarmReceiver extends BroadcastReceiver {

    int medID;

    @Override
    public void onReceive(final Context context, Intent intent) {

        PowerManager powerManager = (PowerManager)context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wakeLock = powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,
                "Medication Reminder");
        wakeLock.acquire(10);


        if (intent != null && intent.hasExtra(InsertActivity.ALARM_EXTRA)){
            medID = intent.getIntExtra(InsertActivity.ALARM_EXTRA, medID);
        }

        Intent i = new Intent(context, InsertActivity.class);
        i.putExtra(InsertActivity.ALARM_EXTRA, medID);
        context.startActivity(i);

        wakeLock.release();
    }
}

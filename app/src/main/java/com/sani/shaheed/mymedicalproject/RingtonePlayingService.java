//package com.sani.shaheed.mymedicalproject;
//
//import android.app.Service;
//import android.content.Intent;
//import android.media.Ringtone;
//import android.media.RingtoneManager;
//import android.net.Uri;
//import android.os.IBinder;
//import android.support.annotation.Nullable;
//
//public class RingtonePlayingService extends Service {
//
//    private Ringtone ringtone = null;
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//
//        final Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
//
//        ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri);
//        ringtone.play();
//
//        AlarmReceiver.completeWakefulIntent(intent);
//        return START_NOT_STICKY;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//
//        if (ringtone != null){
//            ringtone.stop();
//        }
//    }
//}

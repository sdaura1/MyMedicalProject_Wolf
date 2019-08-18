package com.sani.shaheed.mymedicalproject.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.sani.shaheed.mymedicalproject.R;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        Thread thread = new Thread() {
            public void run() {
                try {
                    sleep(2000);
                    startActivity(new Intent(getApplicationContext(), MedList.class));
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}

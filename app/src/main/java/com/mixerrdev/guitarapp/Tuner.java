package com.mixerrdev.guitarapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.TextView;


public class Tuner extends AppCompatActivity {

    AudioProc audioProc;
    TextView result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

        // Hiding Navigation Bar
        ModsUI.hide(this);
        // Keeping screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        result = findViewById(R.id.result);
        audioProc = new AudioProc(this);

        // Start recording
        audioProc.createAudioRecorder();
        audioProc.recordStart();

        // Start reading PCM data and getting frequency
        audioProc.readStart();
    }


    @Override
    protected void onPause() {
        super.onPause();

        audioProc.readStop();
        audioProc.recordStop();
    }

    @Override
    protected void onStop() {
        super.onStop();

        audioProc.readStop();
        audioProc.recordStop();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        audioProc.recordStart();
        audioProc.readStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        audioProc.readStop();
        audioProc.recordStop();

        audioProc.release();
    }
}
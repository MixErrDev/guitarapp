package com.mixerrdev.guitarapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {


    Button tuner;
    Intent intent;

    VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        // Get permission from user
        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            MicPermission.requestMicPermission(this);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Hiding Navigation Bar
        ModsUI.hide(this);
        // Keeping screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        // Video on background
        videoBG = findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.guitarist);
        videoBG.setVideoURI(uri);
        videoBG.start();
        // Loop
        videoBG.setOnPreparedListener(mediaPlayer -> {
            mMediaPlayer = mediaPlayer;
            mMediaPlayer.setLooping(true);

            if(mCurrentVideoPosition != 0) {
                mMediaPlayer.seekTo(mCurrentVideoPosition);
                mMediaPlayer.start();
            }
        });

        // Finding button
        tuner = (Button) findViewById(R.id.tuner);

        // Checking the result of getting permission and starting tuner
        tuner.setOnClickListener((View v) -> startTuner());
    }

    // When permission denied, user can't start tuning
    // user can give permission, when he click on button "start tuning"
    private void startTuner() {
        if (ActivityCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            MicPermission.requestMicPermission(this);
        }
        else {
            intent = new Intent("android.intent.action.tuner");
            startActivity(intent);
        }
    }


    // Overriding onResume for playing video
    @Override
    protected void onResume() {

        super.onResume();
        videoBG.start();
    }
}
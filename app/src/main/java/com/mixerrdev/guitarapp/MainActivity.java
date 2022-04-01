package com.mixerrdev.guitarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button tuner, metronome, lessons;
    VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Hiding navigation bar
        HideNavigationBar.hide(this);


        // Video on background
        videoBG = (VideoView) findViewById(R.id.videoView);
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.guitarist);
        videoBG.setVideoURI(uri);
        videoBG.start();
        // Loop
        videoBG.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mMediaPlayer = mediaPlayer;
                mMediaPlayer.setLooping(true);

                if(mCurrentVideoPosition != 0) {
                    mMediaPlayer.seekTo(mCurrentVideoPosition);
                    mMediaPlayer.start();
                }
            }
        });


        // Buttons
        metronome = (Button) findViewById(R.id.metronome);
        tuner = (Button) findViewById(R.id.tuner);
        lessons = (Button) findViewById(R.id.lessons);

        metronome.setOnClickListener(this);
        tuner.setOnClickListener(this);
        lessons.setOnClickListener(this);
    }


    // Overriding onResume for hiding navigation bar and playing video
    @Override
    protected void onResume() {
        super.onResume();
        HideNavigationBar.hide(this);
        videoBG.start();
    }

    @Override
    public void onClick(View view) {
        Intent intent;

        switch (view.getId()) {
            case R.id.metronome:
                intent = new Intent("android.intent.action.metronome");
                startActivity(intent);
                break;

            case R.id.tuner:
                intent = new Intent("android.intent.action.tuner");
                startActivity(intent);
                break;

            case R.id.lessons:
                intent = new Intent("android.intent.action.lessons");
                startActivity(intent);
                break;
        }
    }
}
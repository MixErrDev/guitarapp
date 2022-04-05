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

public class MainActivity extends AppCompatActivity{
    Button tuner, metronome, lessons;
    Intent intent;

    VideoView videoBG;
    MediaPlayer mMediaPlayer;
    int mCurrentVideoPosition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




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

        metronome.setOnClickListener((View v) -> {
                intent = new Intent("android.intent.action.metronome");
                startActivity(intent);
        });
        tuner.setOnClickListener((View v) -> {
            intent = new Intent("android.intent.action.tuner");
            startActivity(intent);
        });
        lessons.setOnClickListener((View v) -> {
            intent = new Intent("android.intent.action.lessons");
            startActivity(intent);
        });
    }


    // Overriding onResume for hiding navigation bar and playing video
    @Override
    protected void onResume() {
        super.onResume();
        videoBG.start();
    }
}
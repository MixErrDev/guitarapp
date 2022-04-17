package com.mixerrdev.guitarapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Tuner extends AppCompatActivity {

    final String TAG = "myLogs";

    int myBufferSize = 8192;
    AudioRecord audioRecord;
    boolean isReading = false;

    Button startRecord, stopRecord, startRead, stopRead;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

        // Hiding Navigation Bar
        ModsUI.hide(this);

        createAudioRecorder();
        Log.d(TAG, "init state = " + audioRecord.getState());

        startRecord = findViewById(R.id.startRecord);
        stopRecord = findViewById(R.id.stopRecord);
        startRead = findViewById(R.id.startRead);
        stopRead = findViewById(R.id.stopRead);

        startRecord.setOnClickListener((View v) -> {
            Log.d(TAG, "record start");
            audioRecord.startRecording();
            int recordingState = audioRecord.getRecordingState();
            Log.d(TAG, "recordingState = " + recordingState);
        });

        stopRecord.setOnClickListener((View v) -> {
            Log.d(TAG, "record stop");
            audioRecord.stop();
        });

        startRead.setOnClickListener((View v) -> {
            Log.d(TAG, "read start");
            isReading = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if (audioRecord == null)
                        return;

                    byte[] myBuffer = new byte[myBufferSize];
                    int readCount = 0;
                    int totalCount = 0;
                    while (isReading) {
                        readCount = audioRecord.read(myBuffer, 0, myBufferSize);
                        totalCount += readCount;
                        Log.d(TAG, "readCount = " + readCount + ", totalCount = "
                                + totalCount);
                    }
                }
            }).start();
        });

        stopRead.setOnClickListener((View v) -> {
            Log.d(TAG, "read stop");
            isReading = false;
        });
    }

    void createAudioRecorder() {
        int sampleRate = 8000;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

        int minInternalBufferSize = AudioRecord.getMinBufferSize(sampleRate,
                channelConfig, audioFormat);
        int internalBufferSize = minInternalBufferSize * 4;
        Log.d(TAG, "minInternalBufferSize = " + minInternalBufferSize
                + ", internalBufferSize = " + internalBufferSize
                + ", myBufferSize = " + myBufferSize);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC,
                sampleRate, channelConfig, audioFormat, internalBufferSize);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        isReading = false;
        if (audioRecord != null) {
            audioRecord.release();
        }
    }
}
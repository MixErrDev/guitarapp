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
import android.widget.TextView;


public class Tuner extends AppCompatActivity {


    boolean isReading;
    int myBufferSize = 8192;

    final String TAG = "myLogs";
    AudioRecord audioRecord;

    TextView result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);

        // Hiding Navigation Bar
        ModsUI.hide(this);

        result = findViewById(R.id.result);

        // Start recording
        createAudioRecorder();
        Log.d(TAG, "init state = " + audioRecord.getState());
        recordStart();

        // Start reading PCM data and getting frequency
        readStart();
    }

    void createAudioRecorder() {
        int sampleRate = 8000;
        int channelConfig = AudioFormat.CHANNEL_IN_MONO;
        int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

        int minInternalBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
        int internalBufferSize = minInternalBufferSize * 4;

        Log.d(TAG, "minInternalBufferSize = " + minInternalBufferSize
                + ", internalBufferSize = " + internalBufferSize
                + ", myBufferSize = " + internalBufferSize);


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, internalBufferSize);
        }
    }

    public void recordStart() {
        Log.d(TAG, "record start");
        audioRecord.startRecording();
        int recordingState = audioRecord.getRecordingState();
        Log.d(TAG, "recordingState = " + recordingState);
    }

    public void readStart() {
        Log.d(TAG, "read start");
        isReading = true;
        new Thread(() -> {
            while (isReading) {
                if (audioRecord == null)
                    return;

                short[] myBuffer = new short[myBufferSize];
                int readCount;
                int totalCount = 0;

                // Reading PCM in myBuffer
                readCount = audioRecord.read(myBuffer, 0, myBufferSize);
                totalCount += readCount;
                Log.d(TAG, "readCount = " + readCount + ", totalCount = " + totalCount);

                // Getting frequency from PCM
                FrequencyScanner fft = new FrequencyScanner();
                double rst = fft.extractFrequency(myBuffer, 8000);

                // Show frequency
                runOnUiThread(() -> result.setText(Double.toString(rst)));
            }
        }).start();
    }
}
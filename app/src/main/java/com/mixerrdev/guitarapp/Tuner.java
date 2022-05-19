package com.mixerrdev.guitarapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;


public class Tuner extends AppCompatActivity {
    private static int MICROPHONE_PERMISSION_CODE = 200;

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

        if (isMicrophonePresent()) {
            getMicrophonePermission();
        }

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


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
        audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, internalBufferSize);

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
                int d = Log.d(TAG, "fft = " + rst);

                // Show frequency
                runOnUiThread(() -> result.setText(Double.toString(rst)));
            }
        }).start();
    }


    private boolean isMicrophonePresent() {
        if (this.getPackageManager().hasSystemFeature(PackageManager.FEATURE_MICROPHONE)) {
            return true;
        }
        else {
            return false;
        }
    }

    private void getMicrophonePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.RECORD_AUDIO}, MICROPHONE_PERMISSION_CODE);
        }
    }
}
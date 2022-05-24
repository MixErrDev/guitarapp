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
import android.view.WindowManager;
import android.widget.TextView;


public class Tuner extends AppCompatActivity {

    TextView result;


    int sampleRate = 8000;
    int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

    int minInternalBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
    int myBufferSize = minInternalBufferSize * 2;


    boolean isReading;

    final String TAG = "myLogs";
    AudioRecord audioRecord;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);


        // Hiding Navigation Bar
        ModsUI.hide(this);
        // Keeping screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        result = findViewById(R.id.result);

        // Start recording
        createAudioRecorder();
        recordStart();

        // Start reading PCM data and getting frequency
        readStart();
    }


    @Override
    protected void onPause() {
        super.onPause();

        readStop();
        recordStop();
    }

    @Override
    protected void onStop() {
        super.onStop();

        readStop();
        recordStop();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        recordStart();
        readStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        readStop();
        recordStop();

        release();
    }


    public void createAudioRecorder() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
            audioRecord = new AudioRecord(MediaRecorder.AudioSource.MIC, sampleRate, channelConfig, audioFormat, myBufferSize);
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

            if (audioRecord == null)
                return;

            short[] myBuffer = new short[myBufferSize];

            while (isReading) {
                // Reading PCM in myBuffer
                audioRecord.read(myBuffer, 0, myBufferSize);

                // Getting frequency from PCM
                FrequencyScanner fft = new FrequencyScanner();
                double[] rst = fft.extractFrequencyMagnitude(myBuffer, sampleRate);

                // Show frequency
                runOnUiThread(() -> result.setText(Double.toString(rst[0])));

                Log.d(TAG, "frequency = " + rst[0] + "; magnitude = " + rst[1]);

            }
        }).start();
    }

    public void readStop() {
        Log.d(TAG, "read stop");
        isReading = false;
    }

    public void recordStop() {
        Log.d(TAG, "record stop");
        audioRecord.stop();
    }

    public void release() {
        if (audioRecord != null) {
            audioRecord.release();
        }
    }
}
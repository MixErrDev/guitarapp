package com.mixerrdev.guitarapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentContainerView;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class Tuner extends AppCompatActivity {

    // Audio logic vars
    int sampleRate = 8000;
    int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    int audioFormat = AudioFormat.ENCODING_PCM_16BIT;
    int minInternalBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
    int myBufferSize = minInternalBufferSize * 2;
    boolean isReading;
    final String TAG = "myLogs";
    AudioRecord audioRecord;

    // Activity logic vars
    boolean params = false;
    Button buttonParams;
    FragmentContainerView fcv;


    // developing mode vars
    boolean devMode = false;
    int clickCounter = 0;
    TextView frequencyText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);


        // Hiding Navigation Bar
        ModsUI.hide(this);
        // Keeping screen on
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        // Finding
        frequencyText = findViewById(R.id.result);
        buttonParams = findViewById(R.id.buttonParams);
        fcv = findViewById(R.id.fragment);

        // Fragment logic
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, TunerStatus.class, null).commit();

        buttonParams.setOnClickListener((View v) -> {
            if (params) {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, TunerStatus.class, null).commit();
                params = false;
                buttonParams.setText(R.string.params);
            } else {
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, Params.class, null).commit();
                params = true;
                buttonParams.setText(R.string.tuning);
            }
        });

        // Start recording
        createAudioRecorder();
        recordStart();

        // Start reading PCM data and getting frequency
        readStart();

        // Developing mode
        frequencyText.setVisibility(View.INVISIBLE);
        fcv.setOnClickListener((View v) -> {
            clickCounter++;
            if (clickCounter == 2 && devMode) {
                clickCounter = 0;
                frequencyText.setVisibility(View.INVISIBLE);
                devMode = false;
            } else if (clickCounter == 2 ) {
                clickCounter = 0;
                frequencyText.setVisibility(View.VISIBLE);
                devMode = true;
            }
        });
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
                FrequencyMagnitudeScanner fft = new FrequencyMagnitudeScanner();
                double[] rst = fft.extractFrequencyMagnitude(myBuffer, sampleRate);

                // Working with frequency
//                runOnUiThread(() -> frequencyText.setText(Double.toString(rst[0])));
                Log.d(TAG, "frequency = " + rst[0] + "; magnitude = " + rst[1]);

                // TODO CLASSIC -> TRUE/FALSE
                TuningLogic tl = new TuningLogic(true);
                int string = tl.getString(rst[0], rst[1]);
                int status = tl.getStatus(rst[0], rst[1]);

                runOnUiThread(() -> frequencyText.setText(Integer.toString(status)));

                Bundle bundle = new Bundle();
                int[] data = new int[] {string, status};
                bundle.putInt("data", string);
                TunerStatus tunerStatus = new TunerStatus();
                tunerStatus.setArguments(bundle);




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
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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;


public class Tuner extends AppCompatActivity implements OnDataPass{
    // Get data from fragment
    @Override
    public void onDataPass(boolean parameter) {
        classic = parameter;
    }

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
    boolean paramsActive = false;
    Button buttonParams;
    boolean classic = true;



    // developing mode vars
    boolean devMode = false;
    int clickCounter = 0;
    TextView frequencyText, stringText, statusText;


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
        stringText = findViewById(R.id.stringText);
        statusText = findViewById(R.id.statusText);

        // Fragment logic
        Parameters p = new Parameters();
        buttonParams.setOnClickListener((View v) -> {
            if (paramsActive) {
                tuningStart();

                getSupportFragmentManager().beginTransaction().remove(p).commit();
                paramsActive = false;
                buttonParams.setText(R.string.params);
                stringText.setVisibility(View.VISIBLE);
                statusText.setVisibility(View.VISIBLE);

            } else {
                tuningStop();

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment, p, null).commit();
                paramsActive = true;
                buttonParams.setText(R.string.tuning);
                stringText.setVisibility(View.INVISIBLE);
                statusText.setVisibility(View.INVISIBLE);
            }
        });

        // Start reading PCM data and getting frequency
        tuningStart();

        // Developing mode
        frequencyText.setVisibility(View.INVISIBLE);
        statusText.setOnClickListener((View v) -> {
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

        tuningStop();
    }

    @Override
    protected void onStop() {
        super.onStop();
        tuningStop();
    }


    @Override
    protected void onRestart() {
        super.onRestart();
        tuningStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        tuningStop();

        release();
    }




    public void tuningStart() {
        createAudioRecorder();
        recordStart();
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
                Log.d(TAG, "frequency = " + rst[0] + "; magnitude = " + rst[1]);

                TuningLogic tl = new TuningLogic(classic);
                int string = tl.getString(rst[0], rst[1]);
                int status = tl.getStatus(rst[0], rst[1]);



                switch (string) {
                    case (-1):
                    case (0):
                        runOnUiThread(() -> stringText.setText(R.string.unidentified));
                        break;
                    case (1):
                        runOnUiThread(() -> stringText.setText(R.string.first));
                        break;
                    case (2):
                        runOnUiThread(() -> stringText.setText(R.string.second));
                        break;
                    case (3):
                        runOnUiThread(() -> stringText.setText(R.string.third));
                        break;
                    case (4):
                        runOnUiThread(() -> stringText.setText(R.string.fourth));
                        break;
                }

                switch (status) {
                    case (-1):
                        runOnUiThread(() -> statusText.setText(R.string.quiet));
                        break;
                    case (0):
                        runOnUiThread(() -> statusText.setText(R.string.loud));
                        break;
                    case (1):
                        runOnUiThread(() -> statusText.setText(R.string.higher));
                        break;
                    case (2):
                        runOnUiThread(() -> statusText.setText(R.string.lower));
                        break;
                    case (3):
                        runOnUiThread(() -> statusText.setText(R.string.tuned));
                        break;
                }

                runOnUiThread(() -> frequencyText.setText(Double.toString(rst[0])));


            }
        }).start();
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

    public void readStop() {
        Log.d(TAG, "read stop");
        isReading = false;
    }

    public void recordStop() {
        Log.d(TAG, "record stop");
        audioRecord.stop();
    }

    public void tuningStop() {
        recordStop();
        readStop();
    }

    public void release() {
        if (audioRecord != null) {
            audioRecord.release();
        }
    }
}
package com.mixerrdev.guitarapp;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;

public class AudioProc {
    int sampleRate = 8000;
    int channelConfig = AudioFormat.CHANNEL_IN_MONO;
    int audioFormat = AudioFormat.ENCODING_PCM_16BIT;

    int minInternalBufferSize = AudioRecord.getMinBufferSize(sampleRate, channelConfig, audioFormat);
    int myBufferSize = minInternalBufferSize * 2;


    boolean isReading;

    final String TAG = "myLogs";
    AudioRecord audioRecord;

    Context context;
    public AudioProc(Context context) {
        this.context = context;
    }

    public void createAudioRecorder() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
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
//                runOnUiThread(() -> result.setText(Double.toString(rst[0])));

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

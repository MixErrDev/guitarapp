package com.mixerrdev.guitarapp;

import android.Manifest;
import android.app.Activity;

import androidx.core.app.ActivityCompat;


public class MicPermission {
    private static int MICROPHONE_PERMISSION_CODE = 200;

    static void requestMicPermission(Activity activity) {
        try {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.RECORD_AUDIO},
                    MICROPHONE_PERMISSION_CODE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

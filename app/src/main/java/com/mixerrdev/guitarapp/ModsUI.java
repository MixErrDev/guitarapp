package com.mixerrdev.guitarapp;

import android.app.Activity;
import android.os.Build;
import android.view.View;

public class ModsUI {

    // Hiding NavigationBar
    public static void hide(Activity activity) {
        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        final int currentApiVersion = Build.VERSION.SDK_INT;

        if (currentApiVersion >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().getDecorView().setSystemUiVisibility(flags);

            final View decorView = activity.getWindow().getDecorView();
            decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
            {
                @Override
                public void onSystemUiVisibilityChange(int visibility)
                {
                    if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                    {
                        decorView.setSystemUiVisibility(flags);
                    }
                }
            });
        }
    }
}
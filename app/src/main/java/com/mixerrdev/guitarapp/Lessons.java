package com.mixerrdev.guitarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Lessons extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);
        HideNavigationBar.hide(this);
    }
}
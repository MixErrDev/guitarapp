package com.mixerrdev.guitarapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Tuner extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tuner);
        HideNavigationBar.hide(this);
    }
}
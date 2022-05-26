package com.mixerrdev.guitarapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class TunerStatus extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tuner_status,  container, false);
        TextView string = v.findViewById(R.id.string);
        TextView statusText = v.findViewById(R.id.statusText);

        return v;

    }
}
// todo ListView in Fragment
package com.mixerrdev.guitarapp;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class Parameters extends Fragment {
    Button classic, d;
    private OnDataPass mDataPasser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mDataPasser = (OnDataPass) context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_parameters, container, false);
        // Find buttons
        classic = view.findViewById(R.id.classic);
        d = view.findViewById(R.id.d);

        // Send data
        classic.setOnClickListener((View v) -> {
            mDataPasser.onDataPass(true);
            Toast.makeText(getContext(), "Classic tuning parameter", Toast.LENGTH_SHORT).show();
        });

        d.setOnClickListener((View v) -> {
            mDataPasser.onDataPass(false);
            Toast.makeText(getContext(), "D-tuning parameter", Toast.LENGTH_SHORT).show();
        });
        return view;
    }
}
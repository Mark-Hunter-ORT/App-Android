package com.example.markhunters.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.markhunters.R;
import com.example.markhunters.services.sensor.Accelerometer;
import com.example.markhunters.services.sensor.Gyroscope;

public class SensorTestFragment extends Fragment {

    TextView inputZR;
    TextView inputXM;
    float inputZRotation;
    float inputXMovement;
    private Accelerometer accelerometer;
    private Gyroscope gyroscope;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sensor_test, container, false);
        inputZR = rootView.findViewById(R.id.editTextZRotation);
        inputXM = rootView.findViewById(R.id.editTextXMovement);
        rootView.findViewById(R.id.startListeningBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputZRotation = Float.parseFloat(inputZR.getText().toString());
                inputXMovement = Float.parseFloat(inputXM.getText().toString());
                accelerometer = new Accelerometer(getContext());
                gyroscope = new Gyroscope(getContext());
                accelerometer.register();
                gyroscope.register();
                accelerometer.setListener(new Accelerometer.Listener() {
                    @Override
                    public void onTranslation(float tx, float ty, float tz) {
                        if(tx >= inputXMovement) {
                            Toast.makeText(getContext(), "Movimiento X desbloqueado!", Toast.LENGTH_SHORT).show();
                            accelerometer.unregister();
                        }
                    }
                });

                gyroscope.setListener(new Gyroscope.Listener() {
                    @Override
                    public void onRotation(float rx, float ry, float rz) {
                        if(rz >= inputZRotation) {
                            Toast.makeText(getContext(), "Rotación Z desbloqueada!", Toast.LENGTH_SHORT).show();
                            gyroscope.unregister();
                        }
                    }
                });
            }
        });
        return rootView;
    }

}

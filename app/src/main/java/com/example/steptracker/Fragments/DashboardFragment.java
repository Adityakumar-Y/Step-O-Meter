package com.example.steptracker.Fragments;


import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steptracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DashboardFragment extends Fragment implements SensorEventListener{

    private static final String TAG = "DashboardFragment";
    private View view;
    private SensorManager sensorManager;
    private Sensor sensor;
    private int flag = 0;
    private long initialValue = 0, steps = 0;

    @BindView(R.id.tvStepsData)
    TextView tvStepsData;
    @BindView(R.id.tvDistanceData)
    TextView tvDistanceData;
    @BindView(R.id.tvCaloriesData)
    TextView tvCaloriesData;
    @BindView(R.id.tvFloorData)
    TextView tvFloorData;
    @BindView(R.id.tvSpeedData)
    TextView tvSpeedData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupCounterService();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        if(savedInstanceState != null){
            steps = savedInstanceState.getLong("steps", 0);
            tvStepsData.setText(String.valueOf(steps));
        }
        return view;
    }




    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("steps", steps);
    }

    private void setupCounterService() {
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensor != null){
            sensorManager.registerListener(this, sensor,SensorManager.SENSOR_DELAY_FASTEST);
        }else{
            Toast.makeText(getContext(), getString(R.string.no_sensor_msg), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> getActivity().finish(), 3000);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(flag == 0)
        {
            flag = 1;
            initialValue = (long) sensorEvent.values[0];
        }
        steps = (long) (sensorEvent.values[0] - initialValue);
        tvStepsData.setText(String.valueOf(steps));
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}

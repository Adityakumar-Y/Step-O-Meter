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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.steptracker.Adapters.RecordAdapter;
import com.example.steptracker.Models.RecordData;
import com.example.steptracker.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DashboardFragment extends Fragment implements SensorEventListener{

    private static final String TAG = "DashboardFragment";
    @BindView(R.id.gridRecord)
    RecyclerView gridRecord;

    private ArrayList<RecordData> recordList = new ArrayList<>();
    private View view;
    private RecordAdapter recordAdapter;
    private SensorManager sensorManager;
    private Sensor sensor;
    private int flag = 0;
    private long initialValue = 0, steps = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        setupAdapter();
        setupCounterService();
        Log.d(TAG, "onCreateView");
        return view;
    }

    private void setupCounterService() {
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensor != null){
            sensorManager.registerListener(this, sensor,SensorManager.SENSOR_DELAY_UI);
        }else{
            Toast.makeText(getContext(), getString(R.string.no_sensor_msg), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> getActivity().finish(), 3000);
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupRecordGridView();
        Log.d(TAG, "onCreate");
    }

    private void setupRecordGridView() {
        recordList.add(new RecordData(String.valueOf(0.0), getString(R.string.distance_label)));
        recordList.add(new RecordData(String.valueOf(0.0), getString(R.string.calories_label)));
        recordList.add(new RecordData(String.valueOf(0.0), getString(R.string.speed_label)));
        recordList.add(new RecordData(String.valueOf(0.0), getString(R.string.floors_label)));
    }

    private void setupAdapter() {
        recordAdapter = new RecordAdapter(getContext(), recordList);
        gridRecord.setLayoutManager(new GridLayoutManager(getContext(), 2));
        gridRecord.setAdapter(recordAdapter);
    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(flag == 0)
        {
            flag = 1;
            initialValue = (long) sensorEvent.values[0];
        }
        steps = (long) (sensorEvent.values[0] - initialValue);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }
}

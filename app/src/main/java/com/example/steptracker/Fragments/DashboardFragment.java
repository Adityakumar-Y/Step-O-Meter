package com.example.steptracker.Fragments;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.steptracker.Activities.MainActivity;
import com.example.steptracker.Databases.WorkoutDBHelper;
import com.example.steptracker.Contracts.UserContract.WorkoutEntry;
import com.example.steptracker.Models.Record;
import com.example.steptracker.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;


public class DashboardFragment extends Fragment implements SensorEventListener{

    private static final String TAG = "DashboardFragment";
    private View view;
    private SensorManager sensorManager;
    private Sensor sensor1;
    private int flag = 0;
    private long initialValue = 0, steps = 0, calories = 0, dbSteps = 0, dbCalories = 0;
    private float distance = 0f, dbDistance = 0f;
    private String sysDate;

    @BindView(R.id.tvStepsData)
    TextView tvStepsData;
    @BindView(R.id.tvDistanceData)
    TextView tvDistanceData;
    @BindView(R.id.tvCaloriesData)
    TextView tvCaloriesData;
    @BindView(R.id.tvTotalStepsData)
    TextView tvTotalStepsData;
    private float height = 5.5f;
    private float weight = 60;
    private SQLiteDatabase mDatabase;
    private WorkoutDBHelper dbHelper;
    private Cursor cursor;
    private Date date;
    private SimpleDateFormat df;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSystemDate();
        setupCounterService();
    }

    private void getSystemDate() {
        date = Calendar.getInstance().getTime();
        df = new SimpleDateFormat("yyyy-MM-dd");
        sysDate = df.format(date);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onResume() {
        initDatabase();
        initializeValues();
        super.onResume();
    }

    /**
     *  Check for current date in database if present initializes the values and
     *  if not present inserts a new record in the Database
     */
    private void initializeValues() {
        if(!queryDBForDate()){
            insertNewRecord();
            ((MainActivity) getActivity()).recordList.add(new Record(sysDate, 0));
            ((MainActivity)getActivity()).adapter.notifyDataSetChanged();
        }

    }

    private void initDatabase() {
        if(dbHelper == null) {
            dbHelper = new WorkoutDBHelper(getContext());
        }
        mDatabase = dbHelper.getWritableDatabase();
    }


    private void insertNewRecord() {
        ContentValues cv = new ContentValues();
        cv.put(WorkoutEntry.COLUMN_DATE, sysDate);
        cv.put(WorkoutEntry.COLUMN_CALORIES, 0);
        cv.put(WorkoutEntry.COLUMN_DISTANCE, 0);
        cv.put(WorkoutEntry.COLUMN_STEPS, 0);

        mDatabase.insert(WorkoutEntry.TABLE_NAME, null, cv);
        dbSteps = 0;
        dbCalories = 0;
        dbDistance = 0;
    }

    private boolean queryDBForDate() {

        cursor = mDatabase.query(WorkoutEntry.TABLE_NAME,
                null,
                WorkoutEntry.COLUMN_DATE + "=?",
                new String[]{sysDate},
                null,
                null,
                null
        );

        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToNext();

            dbSteps = cursor.getInt(cursor.getColumnIndexOrThrow(WorkoutEntry.COLUMN_STEPS));
            dbCalories = cursor.getInt(cursor.getColumnIndexOrThrow(WorkoutEntry.COLUMN_CALORIES));
            dbDistance = cursor.getFloat(cursor.getColumnIndexOrThrow(WorkoutEntry.COLUMN_DISTANCE));

            tvStepsData.setText(String.valueOf(dbSteps));
            tvTotalStepsData.setText(String.valueOf(dbSteps));
            tvDistanceData.setText(String.format("%.2f", dbDistance));
            tvCaloriesData.setText(String.valueOf(dbCalories));
            return true;
        }

        return false;
    }


    /**
     * Function to get appropriate sensors and registering its listener if present
     */
    private void setupCounterService() {
        sensorManager = (SensorManager) getActivity().getSystemService(Context.SENSOR_SERVICE);
        sensor1 = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(sensor1 != null){
            sensorManager.registerListener(this, sensor1, SensorManager.SENSOR_DELAY_FASTEST);
        } else {
            Toast.makeText(getContext(), getString(R.string.no_sensor_msg), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> getActivity().finish(), 3000);
        }


    }


    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        if(sensorEvent.sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if(!sysDate.equals(df.format(Calendar.getInstance().getTime()))){
                sysDate = df.format(Calendar.getInstance().getTime());
                initializeValues();
                flag = 0;
                Toast.makeText(getContext(), "New Day Started !!", Toast.LENGTH_SHORT).show();
            }
            calculateSteps(sensorEvent);
            calculateDistance();
            calculateCalories();
        }

    }


    private void calculateCalories() {
        calories = Math.round((weight*2.2) * (distance * 0.62) * 0.468);
        tvCaloriesData.setText(String.valueOf(calories));
    }

    private void calculateSteps(SensorEvent sensorEvent) {
        if(flag == 0)
        {
            flag = 1;
            initialValue = (long) sensorEvent.values[0] - dbSteps;
        }
        steps = (long) (sensorEvent.values[0] - initialValue);
        if(steps >= 0) {
            tvStepsData.setText(String.valueOf(steps));
            tvTotalStepsData.setText(String.valueOf(steps));
            updateDatabase();
            updateList();
        }
    }

    private void updateList() {
        if(getActivity() != null) {
            if (((MainActivity) getActivity()).recordList != null && ((MainActivity) getActivity()).recordList.size() > 0) {
                for (int i = 0; i < ((MainActivity) getActivity()).recordList.size(); i++) {
                    String date = ((MainActivity) getActivity()).recordList.get(i).getDate();
                    if (date.equals(sysDate)) {
                        ((MainActivity) getActivity()).recordList.set(i, new Record(sysDate, steps));
                        ((MainActivity) getActivity()).adapter.notifyDataSetChanged();
                    }
                }
            } else {
                ((MainActivity) getActivity()).recordList.add(new Record(sysDate, 0));
                ((MainActivity) getActivity()).adapter.notifyDataSetChanged();
            }
        }


    }

    private void calculateDistance() {
        float strideLength = (float) (height * 0.0003048 * .414);
        distance = (steps * strideLength);
        tvDistanceData.setText(String.format("%.2f", distance));
    }

    private void updateDatabase() {
        initDatabase();
        ContentValues cv = new ContentValues();
        cv.put(WorkoutEntry.COLUMN_CALORIES, calories);
        cv.put(WorkoutEntry.COLUMN_DISTANCE, distance);
        cv.put(WorkoutEntry.COLUMN_STEPS, steps);

        mDatabase.update(
                    WorkoutEntry.TABLE_NAME,
                    cv,
                    WorkoutEntry.COLUMN_DATE + "=?",
                    new String[]{sysDate}
                );
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }


    @Override
    public void onPause() {
        //updateDatabase();
        dbHelper.close();
        super.onPause();
    }
}

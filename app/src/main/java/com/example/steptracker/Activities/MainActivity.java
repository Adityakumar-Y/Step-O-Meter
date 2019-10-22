package com.example.steptracker.Activities;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.steptracker.Adapters.RecordAdapter;
import com.example.steptracker.Adapters.ViewPagerAdapter;
import com.example.steptracker.Contracts.UserContract;
import com.example.steptracker.Databases.WorkoutDBHelper;
import com.example.steptracker.Fragments.DashboardFragment;
import com.example.steptracker.Fragments.GraphFragment;
import com.example.steptracker.Fragments.ProfileFragment;
import com.example.steptracker.Fragments.RegistrationFragment;
import com.example.steptracker.Models.Record;
import com.example.steptracker.R;
import com.example.steptracker.Utils.CustomViewPager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.GONE;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";
    private static final String PREF_NAME = "com.example.steptracker.RegisterPreference";

    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.viewPager)
    CustomViewPager viewPager;
    @BindView(R.id.fragmentContainer)
    FrameLayout container;
    @BindView(R.id.rl_dashboard)
    RelativeLayout rlDashboard;

    public SharedPreferences registerPreference;
    public int isRegistered = 0;

    public ArrayList<Record> recordList = new ArrayList<>();
    public RecordAdapter adapter;
    public WorkoutDBHelper dbHelper;
    public SQLiteDatabase mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        setupAdapter();
        setupDatabase();
        setupPreference();
        setupToolbar();
        setupViewPager();
        checkRegistration();
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
    }


    private void setupViewPager() {
        ViewPagerAdapter adapter = new ViewPagerAdapter (MainActivity.this.getSupportFragmentManager());
        adapter.addFragment(new DashboardFragment());
        adapter.addFragment(new GraphFragment());
        adapter.addFragment(new ProfileFragment());
        viewPager.setAdapter(adapter);
        viewPager.setPagingEnabled(false);
    }

    private void checkRegistration() {
        isRegistered = registerPreference.getInt(getString(R.string.IS_REGISTERED), 0);
        if(isRegistered == 1){
            showLayout();
        }else{
            hideLayout();
            gotoRegistrationForm();
        }
    }

    public void hideLayout() {
        if(rlDashboard.getVisibility() == View.VISIBLE){
            rlDashboard.setVisibility(GONE);
        }
        if(container.getVisibility() == GONE){
            container.setVisibility(View.VISIBLE);
        }
    }

    public void showLayout() {
        if(rlDashboard.getVisibility() == GONE){
            rlDashboard.setVisibility(View.VISIBLE);
        }
        if(container.getVisibility() == View.VISIBLE){
            container.setVisibility(GONE);
        }
    }

    private void gotoRegistrationForm() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, new RegistrationFragment())
                .commit();
    }

    private void setupPreference() {
        registerPreference = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
    }

    private void setupDatabase() {
        dbHelper = new WorkoutDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
    }

    private void setupAdapter() {
        adapter = new RecordAdapter(this, recordList);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.dashboardFragment:
                if(!getSupportActionBar().isShowing()) {
                    getSupportActionBar().show();
                }
                viewPager.setCurrentItem(0);
                return true;
            case R.id.graphFragment:
                if(!getSupportActionBar().isShowing()) {
                    getSupportActionBar().show();
                }
                viewPager.setCurrentItem(1);
                return true;
            case R.id.profileFragment:
                if(getSupportActionBar().isShowing()) {
                    getSupportActionBar().hide();
                }
                viewPager.setCurrentItem(2);
                return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        if(bottomNavigationView.getSelectedItemId() == R.id.dashboardFragment){
            super.onBackPressed();
        }else{
            bottomNavigationView.setSelectedItemId(R.id.dashboardFragment);
        }
    }


    public void getAllData() {
        Cursor cursor = mDatabase.query(UserContract.WorkoutEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        while (cursor.moveToNext()) {
            String date = cursor.getString(
                    cursor.getColumnIndexOrThrow(UserContract.WorkoutEntry.COLUMN_DATE)
            );
            String steps = cursor.getString(
                    cursor.getColumnIndexOrThrow(UserContract.WorkoutEntry.COLUMN_STEPS)
            );
            recordList.add(new Record(date, Long.valueOf(steps)));
        }
    }

    @Override
    protected void onPause() {
        dbHelper.close();
        super.onPause();
    }

}


package com.example.steptracker.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.example.steptracker.Fragments.DashboardFragment;
import com.example.steptracker.Fragments.RegistrationFragment;
import com.example.steptracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final String PREF_NAME = "com.example.steptracker.RegisterPreference";
    public static final String IS_REGISTERED = "IsRegistered";
    @BindView(R.id.fragment_container)
    FrameLayout fragmentContainer;

    public SharedPreferences registerPreference = getSharedPreferences(PREF_NAME, MODE_PRIVATE);;
    private int isRegistered = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        //checkRegistration();
        showRegistrationFragment();
    }

    private void checkRegistration() {
        isRegistered = registerPreference.getInt(IS_REGISTERED, 0);
        if(isRegistered == 0){
            showRegistrationFragment();
        }else{
            showDashboardFragment();
        }
    }

    private void showDashboardFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DashboardFragment())
                .commit();
    }

    private void showRegistrationFragment() {
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new RegistrationFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}


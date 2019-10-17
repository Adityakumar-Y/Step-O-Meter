package com.example.steptracker.Fragments;


import android.content.BroadcastReceiver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.steptracker.Contracts.UserContract;
import com.example.steptracker.Databases.ProfileDBHelper;
import com.example.steptracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {


    private static final String TAG = "ProfileFragment";
    private View view;
    private SQLiteDatabase mDatabase;
    private ProfileDBHelper dbHelper;
    private String name, dob, height, weight;

    @BindView(R.id.tvAccName)
    TextView tvAccName;
    @BindView(R.id.tvAccDob)
    TextView tvAccDob;
    @BindView(R.id.tvAccHeight)
    TextView tvAccHeight;
    @BindView(R.id.tvAccWeight)
    TextView tvAccWeight;
    @BindView(R.id.tvProfileName)
    TextView tvProfileName;
    @BindView(R.id.imgProfile)
    ImageView imgProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        setupDatabase();
        setupProfileData();
        return view;
    }

    private void setupDatabase() {
        dbHelper = new ProfileDBHelper(getContext());
        mDatabase = dbHelper.getReadableDatabase();
    }

    private void setupProfileData() {
        String[] columns = {
                UserContract.ProfileEntry.COLUMN_NAME,
                UserContract.ProfileEntry.COLUMN_DOB,
                UserContract.ProfileEntry.COLUMN_HEIGHT,
                UserContract.ProfileEntry.COLUMN_WEIGHT
        };

        Cursor cursor = mDatabase.query(UserContract.ProfileEntry.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null);

        while(cursor.moveToNext()){
            name = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.ProfileEntry.COLUMN_NAME));
            dob = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.ProfileEntry.COLUMN_DOB));
            height = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.ProfileEntry.COLUMN_HEIGHT));
            weight = cursor.getString(cursor.getColumnIndexOrThrow(UserContract.ProfileEntry.COLUMN_WEIGHT));
        }

        setProfileIcon();

        tvProfileName.setText(name);
        tvAccName.setText(name);
        tvAccDob.setText(dob);
        tvAccHeight.setText(height + " feets");
        tvAccWeight.setText(weight + " kg");
    }

    private void setProfileIcon() {
        String nameInitials = name.substring(0, 2);
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                    .textColor(R.color.colorPrimary)
                    .bold()
                    .toUpperCase()
                .endConfig()
                .buildRoundRect(nameInitials, Color.parseColor("#FFFFB500"), 10);
        imgProfile.setImageDrawable(drawable);
    }

    @Override
    public void onPause() {
        dbHelper.close();
        super.onPause();
    }
}

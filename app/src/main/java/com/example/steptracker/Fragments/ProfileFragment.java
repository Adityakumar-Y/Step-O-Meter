package com.example.steptracker.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.amulyakhare.textdrawable.TextDrawable;
import com.example.steptracker.Activities.MainActivity;
import com.example.steptracker.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private View view;
    private String name, dob, gender;
    private float height, weight;

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
    @BindView(R.id.tvAccGender)
    TextView tvAccGender;
    @BindView(R.id.imgProfile)
    ImageView imgProfile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        ButterKnife.bind(this, view);
        setupProfileData();
        return view;
    }

    private void setupProfileData() {
        name = ((MainActivity) getActivity()).registerPreference.getString(getString(R.string.PREF_NAME), "");
        dob = ((MainActivity) getActivity()).registerPreference.getString(getString(R.string.PREF_DOB), "");
        gender = ((MainActivity) getActivity()).registerPreference.getString(getString(R.string.PREF_GENDER), "");
        height = ((MainActivity) getActivity()).registerPreference.getFloat(getString(R.string.PREF_HEIGHT), 0f);
        weight = ((MainActivity) getActivity()).registerPreference.getFloat(getString(R.string.PREF_WEIGHT), 0f);

        setProfileIcon();

        tvProfileName.setText(name);
        tvAccName.setText(name);
        tvAccDob.setText(dob);
        tvAccHeight.setText(height + " feets");
        tvAccWeight.setText(weight + " kg");
        tvAccGender.setText(gender);
    }

    private void setProfileIcon() {
        String nameInitials = name.substring(0, 2);
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                    .textColor(R.color.colorPrimaryDark)
                    .bold()
                    .toUpperCase()
                .endConfig()
                .buildRoundRect(nameInitials, Color.parseColor("#FFFFB500"), 10);
        imgProfile.setImageDrawable(drawable);
    }
}

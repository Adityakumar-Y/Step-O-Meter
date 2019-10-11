package com.example.steptracker.Fragments;


import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.steptracker.Activities.MainActivity;
import com.example.steptracker.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class RegistrationFragment extends Fragment {


    private static final String TAG = "RegistrationFragment";
    @BindView(R.id.name_edit_text)
    TextInputEditText etName;
    @BindView(R.id.dob_edit_text)
    TextInputEditText etDob;
    @BindView(R.id.height_edit_text)
    TextInputEditText etHeight;
    @BindView(R.id.weight_edit_text)
    TextInputEditText etWeight;
    @BindView(R.id.name_text_input)
    TextInputLayout nameInputLayout;
    @BindView(R.id.dob_text_input)
    TextInputLayout dobInputLayout;
    @BindView(R.id.height_text_input)
    TextInputLayout heightInputLayout;
    @BindView(R.id.weight_text_input)
    TextInputLayout weightInputLayout;
    @BindView(R.id.rb_male)
    RadioButton rbMale;
    @BindView(R.id.rb_female)
    RadioButton rbFemale;
    @BindView(R.id.rg_gender)
    RadioGroup rgGender;
    @BindView(R.id.btn_register)
    Button btnRegister;
    private View view;
    private Calendar calendar;
    private String userName, userDOB, userGender, height, weight;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registration, container, false);
        ButterKnife.bind(this, view);
        calendar = Calendar.getInstance();
        return view;
    }

    DatePickerDialog.OnDateSetListener date = (datePicker, i, i1, i2) -> {
        calendar.set(Calendar.YEAR, i);
        calendar.set(Calendar.MONTH, i1);
        calendar.set(Calendar.DAY_OF_MONTH, i2);
        setDateLabel();
    };

    private void setDateLabel() {
        String format = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);

        etDob.setText(dateFormat.format(calendar.getTime()));
    }

    @OnClick(R.id.dob_edit_text)
    public void onClickDob() {
        new DatePickerDialog(getContext(),
                date,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }

    @OnClick(R.id.btn_register)
    public void registerUser() {
        getUserDetails();
        validateUserData();
    }

    private void getUserDetails() {
        userName = nameInputLayout.getEditText().getText().toString().trim();
        userDOB = dobInputLayout.getEditText().getText().toString().trim();
        height = heightInputLayout.getEditText().getText().toString().trim();
        weight = weightInputLayout.getEditText().getText().toString().trim();
        checkGender();
        Log.d(TAG, "getUserDetails: Name : " + userName + " DOB : " + userDOB + " Height : " + height + " Weight : " + weight + " Gender : " + userGender);
    }

    private void checkGender() {
       int selectedId = rgGender.getCheckedRadioButtonId();
       if(selectedId != -1){
           RadioButton rbSelected = view.findViewById(selectedId);
           userGender = rbSelected.getText().toString();
       } else{
           userGender = "";
       }
    }

    private void validateUserData() {
        // Validation for userName
        if(!TextUtils.isEmpty(userName)){
            if(userName.length() > 2 && userName.length() <= 30){
                nameInputLayout.setError(null);

                //TODO 1: enter into NAME in db
            } else {
                nameInputLayout.setError(getString(R.string.name_length_error_msg));
                return;
            }
        }else{
            nameInputLayout.setError(getString(R.string.empty_name_error_msg));
            return;
        }

        // Validation for DOB
        if(!TextUtils.isEmpty(userDOB)){
            dobInputLayout.setError(null);
            calculateAge(userDOB);

            //TODO 2: enter into DOB and Age in db
        }else{
            dobInputLayout.setError(getString(R.string.empty_dob_error_msg));
            return;
        }


        // Validation for userHeight
        if(!TextUtils.isEmpty(height)){
            float userHeight = Float.valueOf(height);
            if(userHeight > 3 && userHeight <= 8){
                heightInputLayout.setError(null);

                //TODO 3: enter into Height in db
            } else {
                heightInputLayout.setError(getString(R.string.invalide_height_error_msg));
                return;
            }
        }else{
            heightInputLayout.setError(getString(R.string.empty_height_error_msg));
            return;
        }


        // Validation for userWeight
        if(!TextUtils.isEmpty(weight)){
            float userWeight = Float.valueOf(weight);
            if(userWeight > 15 && userWeight < 150){
                weightInputLayout.setError(null);

                //TODO 4: enter into Height in db
            } else {
                weightInputLayout.setError(getString(R.string.invalid_weight_error_msg));
                return;
            }
        }else{
            weightInputLayout.setError(getString(R.string.empty_weight_error_msg));
            return;
        }


        if(!TextUtils.isEmpty(userGender)){
            //TODO 5: enter into Gender in db
        }else{
            Toast.makeText(getContext(), R.string.enpty_gender_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        gotoDashboardFragment();
    }

    private void gotoDashboardFragment() {

        SharedPreferences.Editor editor = ((MainActivity)getActivity()).registerPreference.edit();
        editor.putInt(((MainActivity)getActivity()).IS_REGISTERED, 1);
        editor.commit();

        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new DashboardFragment())
                .addToBackStack(null)
                .commit();
    }

    private void calculateAge(String userDOB) {

    }

}

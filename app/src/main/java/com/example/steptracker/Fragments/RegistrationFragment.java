package com.example.steptracker.Fragments;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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
    private static final int MIN_AGE = 12;
    private static final int MAX_AGE = 75;

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
    private float userHeight, userWeight;
    private DatePickerDialog datePickerDialog;
    private int age = 0;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_registration, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init() {
        calendar = Calendar.getInstance();
    }


    private DatePickerDialog.OnDateSetListener dateListner = (datePicker, i, i1, i2) -> {
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
        datePickerDialog = new DatePickerDialog(getContext(),
                dateListner,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
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
            if(userName.matches("[a-zA-Z ]+")) {
                if (userName.length() > 2 && userName.length() <= 25) {
                    nameInputLayout.setError(null);
                } else {
                    nameInputLayout.setError(getString(R.string.name_length_error_msg));
                    return;
                }
            }else{
                nameInputLayout.setError(getString(R.string.name_isalpha_error_msg));
                return;
            }
        }else{
            nameInputLayout.setError(getString(R.string.empty_name_error_msg));
            return;
        }

        // Validation for DOB
        if(!TextUtils.isEmpty(userDOB)){
            int age = calculateAge(userDOB);
            if(age > MIN_AGE && age < MAX_AGE) {
                dobInputLayout.setError(null);
                Log.d(TAG, "Age : "+ age);
            }else{
                dobInputLayout.setError(getString(R.string.age_restrict_error_msg));
                return;
            }
        }else{
            dobInputLayout.setError(getString(R.string.empty_dob_error_msg));
            return;
        }


        // Validation for userHeight
        if(!TextUtils.isEmpty(height)){
            userHeight = Float.valueOf(height);
            if(userHeight > 3 && userHeight <= 8){
                heightInputLayout.setError(null);
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
            userWeight = Float.valueOf(weight);
            if(userWeight > 15 && userWeight < 150){
                weightInputLayout.setError(null);
            } else {
                weightInputLayout.setError(getString(R.string.invalid_weight_error_msg));
                return;
            }
        }else{
            weightInputLayout.setError(getString(R.string.empty_weight_error_msg));
            return;
        }


        if(TextUtils.isEmpty(userGender)){
            Toast.makeText(getContext() , R.string.enpty_gender_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        gotoDashboard();
    }

    private void gotoDashboard() {

        ((MainActivity) getActivity()).showLayout();

        SharedPreferences.Editor editor = ((MainActivity)getActivity()).registerPreference.edit();
        editor.putString(getString(R.string.PREF_NAME), userName);
        editor.putString(getString(R.string.PREF_DOB), userDOB);
        editor.putString(getString(R.string.PREF_GENDER), userGender);
        editor.putInt(getString(R.string.PREF_AGE), age);
        editor.putFloat(getString(R.string.PREF_HEIGHT), userHeight);
        editor.putFloat(getString(R.string.PREF_WEIGHT), userWeight);
        editor.putLong(getString(R.string.PREF_USER_GOAL), 1000);
        editor.putInt(getString(R.string.IS_REGISTERED), 1);
        editor.commit();


        getFragmentManager().beginTransaction()
                .remove(new RegistrationFragment())
                .commit();
    }

    private int calculateAge(String userDOB) {
        String[] date = userDOB.split("/");
        int day = Integer.parseInt(date[0]);
        int month = Integer.parseInt(date[1]);
        int year = Integer.parseInt(date[2]);

        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();

        dob.set(year, month, day);

        age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);

        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }

        return age;
    }

}

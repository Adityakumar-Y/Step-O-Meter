package com.example.steptracker.Activities;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.steptracker.Contract.UserContract.ProfileEntry;
import com.example.steptracker.Database.ProfileDBHelper;
import com.example.steptracker.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegistrationActivity extends AppCompatActivity {

    private static final String PREF_NAME = "com.example.steptracker.RegisterPreference";
    public static final String IS_REGISTERED = "IsRegistered";
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
    private Calendar calendar;
    private String userName, userDOB, userGender, height, weight;
    private DatePickerDialog datePickerDialog;
    private SharedPreferences registerPreference;
    private int isRegistered = 0;
    private ProfileDBHelper dbHelper;
    private SQLiteDatabase mDatabase;
    private int age = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        ButterKnife.bind(this);
        init();
        setupDatabase();
        checkRegistration();
    }

    private void init() {
        registerPreference = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        calendar = Calendar.getInstance();
    }

    private void setupDatabase() {
        dbHelper = new ProfileDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();
    }

    private void checkRegistration() {
        isRegistered = registerPreference.getInt(IS_REGISTERED, 0);
        if(isRegistered == 1){
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
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
        datePickerDialog = new DatePickerDialog(this,
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
            RadioButton rbSelected = findViewById(selectedId);
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
                    //TODO 1: enter into NAME in db
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
                //TODO 2: enter into DOB and Age in db
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
            Toast.makeText(this , R.string.enpty_gender_msg, Toast.LENGTH_SHORT).show();
            return;
        }

        gotoMainActivity();
    }

    private void gotoMainActivity() {
        SharedPreferences.Editor editor = registerPreference.edit();
        editor.putInt(IS_REGISTERED, 1);
        editor.commit();

        ContentValues cv = new ContentValues();
        cv.put(ProfileEntry.COLUMN_NAME, userName);
        cv.put(ProfileEntry.COLUMN_DOB, userDOB);
        cv.put(ProfileEntry.COLUMN_AGE, age);
        cv.put(ProfileEntry.COLUMN_GENDER, userGender);
        cv.put(ProfileEntry.COLUMN_HEIGHT, height);
        cv.put(ProfileEntry.COLUMN_WEIGHT, weight);

        mDatabase.insert(ProfileEntry.TABLE_NAME, null, cv);

        startActivity(new Intent(this, MainActivity.class));
        finish();
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

    @Override
    protected void onPause() {
        dbHelper.close();
        super.onPause();
    }
}

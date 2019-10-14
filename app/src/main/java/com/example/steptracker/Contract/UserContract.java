package com.example.steptracker.Contract;

import android.provider.BaseColumns;

public class UserContract {

    public UserContract() {
    }

    public static final class ProfileEntry implements BaseColumns{
        public static final String TABLE_NAME = "Profile";
        public static final String COLUMN_NAME = "Name";
        public static final String COLUMN_DOB = "DateOfBirth";
        public static final String COLUMN_AGE = "Age";
        public static final String COLUMN_GENDER = "Gender";
        public static final String COLUMN_HEIGHT = "Height";
        public static final String COLUMN_WEIGHT = "Weight";
    }
}

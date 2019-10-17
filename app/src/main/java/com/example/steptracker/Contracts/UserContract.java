package com.example.steptracker.Contracts;

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

    public static final class WorkoutEntry implements BaseColumns{
        public static final String TABLE_NAME = "Workout";
        public static final String COLUMN_DATE = "Date";
        public static final String COLUMN_STEPS = "Steps";
        public static final String COLUMN_DISTANCE = "Distance";
        public static final String COLUMN_CALORIES = "Calories";
        //public static final String COLUMN_FLOORS = "Floors";
    }
}

package com.example.steptracker.Contracts;

import android.provider.BaseColumns;

public class UserContract {

    public UserContract() {
    }

    public static final class WorkoutEntry implements BaseColumns{
        public static final String TABLE_NAME = "Workout";
        public static final String COLUMN_DATE = "Date";
        public static final String COLUMN_STEPS = "Steps";
        public static final String COLUMN_DISTANCE = "Distance";
        public static final String COLUMN_CALORIES = "Calories";
    }
}

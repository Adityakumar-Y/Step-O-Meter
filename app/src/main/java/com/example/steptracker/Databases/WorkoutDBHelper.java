package com.example.steptracker.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import com.example.steptracker.Contracts.UserContract.WorkoutEntry;

import java.io.File;

public class WorkoutDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "UserWorkout.db";
    public static final int DATABASE_VERSION = 1;
    private static final String FILE_DIR = "Step-O-Meter";

    public WorkoutDBHelper(Context context){
        super(context, Environment.getExternalStorageDirectory()
                + File.separator + FILE_DIR
                + File.separator +DATABASE_NAME, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                WorkoutEntry.TABLE_NAME + " (" +
                WorkoutEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                WorkoutEntry.COLUMN_DATE + " STRING UNIQUE NOT NULL , " +
                WorkoutEntry.COLUMN_STEPS + " INTEGER NOT NULL, " +
                WorkoutEntry.COLUMN_CALORIES + " INTEGER NOT NULL, " +
                WorkoutEntry.COLUMN_DISTANCE + " REAL NOT NULL" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ WorkoutEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

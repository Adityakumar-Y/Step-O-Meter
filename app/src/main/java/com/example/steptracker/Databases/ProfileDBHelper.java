package com.example.steptracker.Databases;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.steptracker.Contracts.UserContract.ProfileEntry;

public class ProfileDBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "UserProfile.db";
    public static final int DATABASE_VERSION = 1;

    public ProfileDBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_TABLE = "CREATE TABLE " +
                ProfileEntry.TABLE_NAME + " (" +
                ProfileEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ProfileEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                ProfileEntry.COLUMN_DOB + " TEXT NOT NULL, " +
                ProfileEntry.COLUMN_AGE + " TEXT NOT NULL, " +
                ProfileEntry.COLUMN_GENDER + " TEXT NOT NULL, " +
                ProfileEntry.COLUMN_HEIGHT + " TEXT NOT NULL, " +
                ProfileEntry.COLUMN_WEIGHT + " TEXT NOT NULL" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ ProfileEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}

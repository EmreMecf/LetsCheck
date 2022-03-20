package com.example.letscheck.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class TrialDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "Trial";
    public static final String TABLE_TRIAL = "trial";
    private static final int DB_VERSION = 1;

    public TrialDatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        try{
            sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS "+TABLE_TRIAL+" (id INTEGER PRIMARY KEY, trialname VARCHAR, correct REAL, wrong REAL)");
        }catch (SQLiteException ex){
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}

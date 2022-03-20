package com.example.letscheck.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import androidx.annotation.Nullable;
import com.example.letscheck.Trial;

import java.util.ArrayList;
import java.util.List;

public class TrialDaoImpl implements TrialDao  {

    private final SQLiteDatabase database;

    public TrialDaoImpl(SQLiteDatabase database) {
        this.database = database;
    }

    @Override
    public List<Trial> getAll() {
        List<Trial> trialList = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT * FROM " + TrialDatabaseHelper.TABLE_TRIAL, null);
        int trialNameIx = cursor.getColumnIndex("trialname");
        int idIx = cursor.getColumnIndex("id");
        while (cursor.moveToNext()){
            int id = cursor.getInt(idIx);
            String trialName = cursor.getString(trialNameIx);
            Trial trial = new Trial(trialName, id);
            trialList.add(trial);
        }
        cursor.close();
        return trialList;
    }

    @Override
    @Nullable
    public Trial get(int id) {
        Trial trial = null;
        Cursor cursor = database.rawQuery("SELECT * FROM " + TrialDatabaseHelper.TABLE_TRIAL+" WHERE id =? ", new String[]{String.valueOf(id)});
        int trialNameIx = cursor.getColumnIndex("trialname");
        int idIx = cursor.getColumnIndex("id");
        int correctIx = cursor.getColumnIndex("correct");
        int wrongIx = cursor.getColumnIndex("wrong");

        if (cursor.getCount() > 0){
            cursor.moveToFirst();
            int trialId = cursor.getInt(idIx);
            String trialName = cursor.getString(trialNameIx);
            double correct = cursor.getDouble(correctIx);
            double wrong = cursor.getDouble(wrongIx);

            trial = new Trial(trialName,trialId,correct,wrong);
        }
        cursor.close();
        return trial;
    }

    @Override
    public int delete(int id) {
        database.execSQL("DELETE FROM trial WHERE id=" + id);
        return id;
    }

    @Override
    public void add(Trial trial) {
        String sqlString = "INSERT INTO trial ( trialname, correct, wrong) VALUES ('" + trial.getName() + "'," + trial.getCorrect() + "," + trial.getWrong() +")";
        database.execSQL(sqlString);
    }

    @Override
    public void update(Trial trial){
    }




}

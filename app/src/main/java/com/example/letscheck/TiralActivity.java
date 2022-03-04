package com.example.letscheck;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.letscheck.databinding.ActivityTiralAddBinding;

import java.util.Objects;

public class TiralActivity extends AppCompatActivity {
    private ActivityTiralAddBinding binding;

    EditText editText;
    EditText editText1;
    TextView textView;
    SQLiteDatabase database;

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTiralAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        database = this.openOrCreateDatabase("Trial", MODE_PRIVATE, null);
        editText = findViewById(R.id.correct);
        editText1 = findViewById(R.id.wrong);
        textView = findViewById(R.id.net);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent=getIntent();
        String info=intent.getStringExtra("info");

        if (info.equals("new")){
            //new
            binding.trialname.setText("");
            binding.wrong.setText("");
            binding.correct.setText("");
            binding.ekle.setVisibility(View.VISIBLE);

        }else{
            int artId =intent.getIntExtra("artId",0);
            binding.ekle.setVisibility(View.INVISIBLE);
            try {
                Cursor cursor=database.rawQuery("SELECT * FROM trial WHERE id=?",new String[]{String.valueOf(artId)});
                int trialNameIx =cursor.getColumnIndex("trialname");
                int correctIx =cursor.getColumnIndex("correct");
                int wrongIx=cursor.getColumnIndex("wrong");
                int netIx =cursor.getColumnIndex("net");
                while (cursor.moveToNext()){
                    binding.trialname.setText(cursor.getString(trialNameIx));
                    binding.correct.setText(cursor.getString(correctIx));
                    binding.wrong.setText(cursor.getString(wrongIx));
                    binding.net.setText(cursor.getString(netIx));
                }
                cursor.close();



            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }


    public void ekle(View view) {

        String trialName = binding.trialname.getText().toString();
        String correct = binding.correct.getText().toString();
        String wrong = binding.wrong.getText().toString();

        if (editText.getText().toString().matches("") || editText1.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "Doğru veya Yanlış girmediniz", Toast.LENGTH_LONG).show();

        } else {
            float dogru = Float.parseFloat(editText.getText().toString());
            float yanlıs = Float.parseFloat(editText1.getText().toString());

            float net2 = yanlıs / 4;
            float net1 = dogru - net2;
            textView.setText("Net:"+net1);




            try {
                database.execSQL("CREATE TABLE IF NOT EXISTS trial(id INTEGER PRIMARY KEY, trialname VARCHAR, correct VARCHAR, wrong VARCHAR, net REAL)");
                String sqlString = "INSERT INTO trial ( trialname, correct, wrong, net) VALUES ('" + trialName + "'," + correct + "," + wrong + "," + net1 + ")";
                database.execSQL(sqlString);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        Intent intent = new Intent(TiralActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);


    }
}
package com.example.letscheck;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.letscheck.databinding.ActivityTiralAddBinding;

import java.util.Objects;

public class TiralActivity extends AppCompatActivity {
    private ActivityTiralAddBinding binding;

    EditText correctEt;
    EditText wrongEt;
    TextView netTv;
    SQLiteDatabase database;

    float getDogru() {
        String dogruStr = correctEt.getText().toString();
        if (dogruStr.isEmpty()) return 0;
        return Float.parseFloat(dogruStr);
    }

    float getYanlıs() {
        String yanlısStr = wrongEt.getText().toString();
        if (yanlısStr.isEmpty()) return 0;
        return Float.parseFloat(yanlısStr);
    }

    TextWatcher countNetWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            float dogru = getDogru();
            float yanlıs = getYanlıs();
            float net = countNet(dogru, yanlıs);

            netTv.setText("Net:" + net);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTiralAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        database = this.openOrCreateDatabase("Trial", MODE_PRIVATE, null);
        correctEt = findViewById(R.id.correct);
        wrongEt = findViewById(R.id.wrong);
        netTv = findViewById(R.id.net);
        correctEt.addTextChangedListener(countNetWatcher);
        wrongEt.addTextChangedListener(countNetWatcher);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String info = intent.getStringExtra("info");

        if (info.equals("new")) {
            //new
            binding.trialname.setText("");
            binding.wrong.setText("");
            binding.correct.setText("");
            binding.ekle.setVisibility(View.VISIBLE);

        } else {
            int artId = intent.getIntExtra("artId", 0);
            binding.ekle.setVisibility(View.INVISIBLE);
            try {
                Cursor cursor = database.rawQuery("SELECT * FROM trial WHERE id=?", new String[]{String.valueOf(artId)});
                int trialNameIx = cursor.getColumnIndex("trialname");
                int correctIx = cursor.getColumnIndex("correct");
                int wrongIx = cursor.getColumnIndex("wrong");
                int netIx = cursor.getColumnIndex("net");
                while (cursor.moveToNext()) {
                    binding.trialname.setText(cursor.getString(trialNameIx));
                    binding.correct.setText(cursor.getString(correctIx));
                    binding.wrong.setText(cursor.getString(wrongIx));
                    binding.net.setText(cursor.getString(netIx));
                }
                cursor.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }


    public void ekle(View view) {

        String trialName = binding.trialname.getText().toString();
        String correct = binding.correct.getText().toString();
        String wrong = binding.wrong.getText().toString();

        if (correctEt.getText().toString().matches("") || wrongEt.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "Doğru veya Yanlış girmediniz", Toast.LENGTH_LONG).show();

        } else {

            float dogru = Float.parseFloat(correctEt.getText().toString());
            float yanlıs = Float.parseFloat(wrongEt.getText().toString());

            float net1 = countNet(dogru, yanlıs);

            netTv.setText(""  +  net1);

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

    private float countNet(float dogru, float yanlıs) {
        float net2 = yanlıs / 4;
        float net1 = dogru - net2;

        return net1;
    }
}
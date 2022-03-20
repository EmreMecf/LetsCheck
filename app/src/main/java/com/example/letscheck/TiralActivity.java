package com.example.letscheck;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.letscheck.databinding.ActivityTiralAddBinding;
import com.example.letscheck.db.TrialDatabaseHelper;
import com.example.letscheck.db.TrialDaoImpl;

import java.util.Objects;

public class TiralActivity extends AppCompatActivity {
    private ActivityTiralAddBinding binding;

    private EditText correctEt;
    private EditText wrongEt;
    private TextView netTv;
    private TrialDatabaseHelper databaseHelper;
    private TrialDaoImpl trialDao;

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

        databaseHelper = new TrialDatabaseHelper(this);
        trialDao = new TrialDaoImpl(databaseHelper.getWritableDatabase());

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
            bindData();
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }

    private void bindData(){
        int artId = getIntent().getIntExtra("artId", 0);
        binding.ekle.setVisibility(View.INVISIBLE);
        try {
            Trial trial = trialDao.get(artId);
            if (trial == null)
                return;
            binding.trialname.setText(trial.getName());
            binding.correct.setText(String.valueOf(trial.getCorrect()));
            binding.wrong.setText(String.valueOf(trial.getWrong()));
            float net = countNet((float)trial.getCorrect(),(float)trial.getWrong());
            binding.net.setText(String.valueOf(net));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ekle(View view) {

        String trialName = binding.trialname.getText().toString();

        if (correctEt.getText().toString().matches("") || wrongEt.getText().toString().matches("")) {
            Toast.makeText(getApplicationContext(), "Doğru veya Yanlış girmediniz", Toast.LENGTH_LONG).show();

        } else {

            float dogru = getDogru();
            float yanlıs = getYanlıs();

            float net1 = countNet(dogru, yanlıs);
            netTv.setText(""  +  net1);

            try {
                Trial trial = new Trial(trialName,dogru,yanlıs);
                trialDao.add(trial);
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
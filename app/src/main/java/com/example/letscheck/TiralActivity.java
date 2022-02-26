package com.example.letscheck;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.letscheck.databinding.ActivityTiralAddBinding;

public class TiralActivity extends AppCompatActivity {
    private ActivityTiralAddBinding binding;

    EditText editText;
    EditText editText1;
    TextView textView;
    SQLiteDatabase database;

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

        Intent intent=getIntent();
        String info=intent.getStringExtra("info");

        if (info.equals("new")){
            //new
            binding.trialname.setText("");
            binding.wrong.setText("");
            binding.correct.setText("");
            binding.net.setText("");
            binding.ekle.setVisibility(View.VISIBLE);

        }else{
            int artId =intent.getIntExtra("artId",0);
            binding.ekle.setVisibility(View.INVISIBLE);
            try {
                Cursor cursor=database.rawQuery("SELECT * FROM trial WHERE id=?",new String[]{String.valueOf(artId)});
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }


    public void ekle(View view) {

        String trialName = binding.trialname.getText().toString();
        String correct = binding.correct.getText().toString();
        String wrong = binding.wrong.getText().toString();
        String net = binding.net.getText().toString();

        if (editText.getText().toString().matches(" ") || editText1.getText().toString().matches(" ")) {
            Toast.makeText(getApplicationContext(), "Doğru veya Yanlış girmediniz", Toast.LENGTH_LONG).show();

        } else {
            float dogru = Float.parseFloat(editText.getText().toString());
            float yanlıs = Float.parseFloat(editText1.getText().toString());

            float net2 = yanlıs / 4;
            float net1 = dogru - net2;

            textView.setText("NET :" + net1);

            try {
                database.execSQL("CREATE TABLE IF NOT EXISTS trial(id INTEGER PRIMARY KEY, trialname VARCHAR, correct VARCHAR, wrong VARCHAR, net VARCHAR)");
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
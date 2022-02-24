package com.example.letscheck;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.example.letscheck.databinding.ActivityTiralAddBinding;

public class TiralAdd extends AppCompatActivity {
    private ActivityTiralAddBinding binding;

    EditText editText;
    EditText editText1;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityTiralAddBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        editText=findViewById(R.id.dogru);
        editText1=findViewById(R.id.yanlıs);
        textView=findViewById(R.id.net);
    }


    public void ekle(View view){
        if (editText.getText().toString().matches(" ") || editText1.getText().toString().matches(" ")){
            Toast.makeText(getApplicationContext(),"Doğru veya Yanlış girmediniz",Toast.LENGTH_LONG).show();

        }else{
            float dogru=Float.parseFloat(editText.getText().toString());
            float yanlıs=Float.parseFloat(editText1.getText().toString());

            float net =yanlıs/4;
            float net1=dogru-net;

            textView.setText("NET :" + net1);

        }

    }
}
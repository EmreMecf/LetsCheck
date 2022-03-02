package com.example.letscheck;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.letscheck.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    SQLiteDatabase sqLiteDatabase;
    ArrayList<Trial> trialArrayList;
    TrialAdapter trialAdapter;
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            final Trial trial = trialArrayList.get(position);
            switch (view.getId()) {
                case  R.id.image_delete:
                    deleteItem(trial);
                    break;
                default:
                    goToTrialActivity(trial);
            }
        }
    };

    private void deleteItem(Trial trial) {
        int id=trial.id;
        sqLiteDatabase.execSQL("DELETE FROM trial WHERE id="+id);
        getData();
    }

    private void goToTrialActivity(Trial trial) {
        Intent intent=new Intent(getApplicationContext(),TiralActivity.class);
        intent.putExtra("info","old");
        intent.putExtra("artId",trial.id);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        trialArrayList = new ArrayList<>();
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        trialAdapter = new TrialAdapter(trialArrayList);
        trialAdapter.setOnItemClickListener(onItemClickListener);
        binding.recyclerView.setAdapter(trialAdapter);

        sqLiteDatabase = this.openOrCreateDatabase("Trial", MODE_PRIVATE, null);
        getData();
    }

    private void getData() {

        try {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM trial", null);
            int nameIx = cursor.getColumnIndex("trialname");
            int idIx = cursor.getColumnIndex("id");
            int imageIx=cursor.getColumnIndex("image");

            trialArrayList.clear();
            while (cursor.moveToNext()) {
                String name = cursor.getString(nameIx);
                int id = cursor.getInt(idIx);
                Trial trial = new Trial(name, id);
                trialArrayList.add(trial);
            }
            trialAdapter.notifyDataSetChanged();
            cursor.close();


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.add_raw, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_trial) {
            Intent intent = new Intent(this, TiralActivity.class);
            intent.putExtra("info","new");
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

}
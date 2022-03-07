package com.example.letscheck;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.letscheck.databinding.ActivityMainBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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
                case R.id.popup_menu:
                    showPopupMenu(view, trial);
                    break;
                default:
                    goToTrialActivity(trial);
            }
        }
    };

    private void showPopupMenu(View v, Trial trial) {
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.delete:
                        deleteItem(trial);
                        return true;
                }
                return false;
            }
        });
        popupMenu.show();

    }

    private void deleteItem(Trial trial) {
        int id = trial.id;
        sqLiteDatabase.execSQL("DELETE FROM trial WHERE id=" + id);
        getData();
    }

    private void goToTrialActivity(Trial trial) {
        Intent intent = new Intent(MainActivity.this,TiralActivity.class);
        intent.putExtra("info", "old");
        intent.putExtra("artId", trial.id);
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

        FloatingActionButton fab =findViewById(R.id.add_trial);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId()==R.id.add_trial) {
                    Intent intent = new Intent(MainActivity.this, TiralActivity.class);
                    intent.putExtra("info", "new");
                    startActivity(intent);
                }
            }
        });

        sqLiteDatabase = this.openOrCreateDatabase("Trial", MODE_PRIVATE, null);
        getData();
    }

    private void getData() {

        try {
            Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM trial", null);
            int nameIx = cursor.getColumnIndex("trialname");
            int idIx = cursor.getColumnIndex("id");

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


}
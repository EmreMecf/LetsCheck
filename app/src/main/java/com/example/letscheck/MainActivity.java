package com.example.letscheck;

import android.content.Intent;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.letscheck.databinding.ActivityMainBinding;
import com.example.letscheck.db.TrialDatabaseHelper;
import com.example.letscheck.db.TrialDao;
import com.example.letscheck.db.TrialDaoImpl;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    TrialAdapter trialAdapter;
    private SQLiteOpenHelper databaseHelper;
    private TrialDao trialDao;

    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            final Trial trial = trialAdapter.getTrialArrayList().get(position);
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
        trialDao.delete(trial.getId());
        getAllTrialList();
    }

    private void goToTrialActivity(Trial trial) {
        Intent intent = new Intent(MainActivity.this,TiralActivity.class);
        intent.putExtra("info", "old");
        intent.putExtra("artId", trial.getId());
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplicationContext().startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        databaseHelper = new TrialDatabaseHelper(this);
        trialDao = new TrialDaoImpl(databaseHelper.getWritableDatabase());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        trialAdapter = new TrialAdapter();
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

        getAllTrialList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        databaseHelper.close();
    }

    private void getAllTrialList() {

        try {
            List<Trial> trialList = trialDao.getAll();
            trialAdapter.setTrialArrayList(trialList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
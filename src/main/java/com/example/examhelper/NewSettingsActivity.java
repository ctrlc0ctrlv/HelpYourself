package com.example.examhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class NewSettingsActivity extends AppCompatActivity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sets);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().getItem(2).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_map:
                                item.setChecked(true);
                                finish();
                                Intent intent0 = new Intent(getBaseContext(), Show_DB_Activity.class);
                                startActivity(intent0);
                                break;
                            case R.id.action_mail:
                                item.setChecked(true);
                                finish();
                                Intent intent1 = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent1);
                                break;
                            case R.id.action_settings:
                                item.setChecked(true);
                                finish();
                                Intent intent2 = new Intent(getBaseContext(), NewSettingsActivity.class);
                                startActivity(intent2);
                        }
                        return false;
                    }
                });
    }
}

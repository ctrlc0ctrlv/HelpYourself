package com.example.examhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("myLogs","OnCreate");
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(this); // Обработчик нажатия кнопки

        String night_mode = PreferenceManager.getDefaultSharedPreferences(this).getString("night_mode","Включать автоматически");
        assert night_mode != null;
        int modeNight = 0;
        switch (night_mode){
            case ("Включать автоматически"):

                break;
            case ("Да"):
                modeNight = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            case ("Нет"):
                modeNight = AppCompatDelegate.MODE_NIGHT_NO;
                break;
        }
        AppCompatDelegate.setDefaultNightMode(modeNight);
    }

    public void onClick(View view){
        Intent i;
            i = new Intent(this,ChooseIntentionActivity.class);
            startActivity(i);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem action_delete_progress = menu.findItem(R.id.action_delete_progress);
        MenuItem action_reload_task = menu.findItem(R.id.action_reload_task);
        MenuItem action_clear_database = menu.findItem(R.id.action_clear_database);

        action_reload_task.setVisible(false);
        action_delete_progress.setVisible(false);
        action_clear_database.setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return true;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
        Log.d("myLogs","OnResume");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // читаем размер шрифта из EditTextPreference
        Float fSize = Float.parseFloat(Objects.requireNonNull(prefs.getString(getString(R.string.pref_size), "14")));
        // применяем настройки в текстовом поле
        TextView text_view = findViewById(R.id.text_view);
        text_view.setTextSize(TypedValue.COMPLEX_UNIT_SP,fSize);

        String regular = prefs.getString(getString(R.string.pref_style), "");
        int typeface = Typeface.NORMAL;
        assert regular != null;
        if (regular.contains("Полужирный"))
            typeface += Typeface.BOLD;
        if (regular.contains("Курсив"))
            typeface += Typeface.ITALIC;
        // меняем настройки в TextView
        text_view.setTypeface(null, typeface);

        String night_mode = prefs.getString("night_mode","Включать автоматически");
        assert night_mode != null;
        int modeNight = 0;
        switch (night_mode){
            case ("Включать автоматически"):

                break;
            case ("Да"):
                modeNight = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            case ("Нет"):
                modeNight = AppCompatDelegate.MODE_NIGHT_NO;
                break;
        }
        AppCompatDelegate.setDefaultNightMode(modeNight);
    }

    @Override
    public void onStop(){
        super.onStop();
        Log.d("myLogs","onStop");
    }
}

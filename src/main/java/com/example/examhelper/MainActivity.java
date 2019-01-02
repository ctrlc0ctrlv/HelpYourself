package com.example.examhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    static {
        //подключаем возможность изменения цветовой схемы на ночной режим автоматически
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(this); // Обработчик нажатия кнопки
    }

    public void onClick(View view){
        Intent i;
            i = new Intent(this,ChooseIntentionActivity.class);
            startActivity(i);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
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
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // читаем размер шрифта из EditTextPreference
        Integer fSize = Integer.parseInt(Objects.requireNonNull(prefs.getString(getString(R.integer.pref_size), "14")));
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
    }
    }

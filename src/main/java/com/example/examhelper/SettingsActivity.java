package com.example.examhelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.widget.Toast;

public class SettingsActivity extends PreferenceActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        // загружаем предпочтения из ресурсов
        addPreferencesFromResource(R.xml.preferences);

        Context context = getApplicationContext();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);

        Log.d("myLogs","OnCreateSettings");

        Preference change_notifications = findPreference("change_notifications");
        change_notifications.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getApplicationContext(), TimeActivity.class);
                startActivity(intent);
                return false;
            }
        });

        Preference about = findPreference("about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getApplicationContext(), FragmentsActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        switch (key){
            case ("night_mode"):
                Log.d("myLogs","OnChangeSettings");
                String night_mode = preferences.getString("night_mode", "Нет");
                assert night_mode != null;
                int modeNight = 0;
                switch (night_mode){
                    case ("Да"):
                        modeNight = AppCompatDelegate.MODE_NIGHT_YES;
                        break;
                    case ("Нет"):
                        modeNight = AppCompatDelegate.MODE_NIGHT_NO;
                        break;
                }

                Toast.makeText(getApplicationContext(), "Рекомендуем перезапустить приложение для корректной работы ночного режима", Toast.LENGTH_SHORT).show();

                AppCompatDelegate.setDefaultNightMode(modeNight);
                finish();
                break;
            case ("seekBarPreference"):
                int f_Size = preferences.getInt("seekBarPreference", 25);
                Log.d("myLogs", "Изменен размер текста на " + f_Size);

                Resources res = getResources();
                Configuration configuration = new Configuration(res.getConfiguration());

                final float start_value = 0.8f; //начальное значение размера шрифта
                //final float max_start_value = 1.6f;
                final float step = 0.016f; //шаг увеличения коэффициента

                configuration.fontScale = start_value + step * f_Size;
                res.updateConfiguration(configuration, res.getDisplayMetrics());
                Toast.makeText(getBaseContext(), "Рекомендуем перезапустить приложение для применения настроек", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}

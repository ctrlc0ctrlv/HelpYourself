package com.example.examhelper;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

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
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        switch (key){
            case ("night_mode"):
                Log.d("myLogs","OnChangeSettings");
                String night_mode = preferences.getString("night_mode","Включать автоматически");
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
                finish();
                break;
            case ("Размер шрифта"):
                Log.d("myLogs","Изменен размер текста");
                break;
        }
    }
}

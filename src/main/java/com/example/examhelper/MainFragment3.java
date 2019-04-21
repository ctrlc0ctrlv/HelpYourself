package com.example.examhelper;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

public class MainFragment3 extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);

        Context context = getActivity();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        prefs.registerOnSharedPreferenceChangeListener(this);

        Log.d("myLogs", "OnCreateSettings");

        Preference change_notifications = findPreference("change_notifications");
        change_notifications.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getActivity(), TimeActivity.class);
                startActivity(intent);
                return false;
            }
        });

        Preference about = findPreference("about");
        about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                //Intent intent = new Intent(getActivity(), AboutActivity.class);
                Intent intent = new Intent(getActivity(), ThreadActivity.class);
                startActivity(intent);
                return false;
            }
        });
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences, String key) {
        switch (key) {
            case ("night_mode"):
                Log.d("myLogs", "OnChangeSettings");
                Log.d("myLogs", String.valueOf(getActivity()));

                try {
                    this.getActivity().recreate();
                } catch (Exception e) {
                    Log.d("myLogs", "exception: " + e);
                }
                //Toast.makeText(getActivity(), "Рекомендуем перезапустить приложение для корректной работы ночного режима", Toast.LENGTH_SHORT).show();
                //AppCompatDelegate.setDefaultNightMode(modeNight);
                break;
            case ("seekBarPreference"):
                try {
                    this.getActivity().recreate();
                } catch (Exception e) {
                    Log.d("myLogs", "fontexception: " + e);
                }
                /*int f_Size = preferences.getInt("seekBarPreference", 25);
                Log.d("myLogs", "Изменен размер текста на " + f_Size);

                Resources res = getActivity().getResources();
                Configuration configuration = new Configuration(res.getConfiguration());

                final float start_value = 0.8f; //начальное значение размера шрифта
                //final float max_start_value = 1.6f;
                final float step = 0.016f; //шаг увеличения коэффициента

                configuration.fontScale = start_value + step * f_Size;
                res.updateConfiguration(configuration, res.getDisplayMetrics());*/
                //Toast.makeText(getActivity().getBaseContext(), "Рекомендуем перезапустить приложение для применения настроек", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("myLogs", "fragment detached");
    }
}

package com.example.examhelper;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Intent intent;

    @Override
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
            case ("Да"):
                modeNight = AppCompatDelegate.MODE_NIGHT_YES;
                break;
            case ("Нет"):
                modeNight = AppCompatDelegate.MODE_NIGHT_NO;
                break;
        }
        AppCompatDelegate.setDefaultNightMode(modeNight);

        final Intent intent0 = new Intent(this, Show_DB_Activity.class);
        final Intent intent1 = new Intent(this, MainActivity.class);

        final TextView text_view = findViewById(R.id.text_view);
        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_map:
                                text_view.setText("Задания");
                                RadioGroup radioGroup = findViewById(R.id.radioGroup);
                                switch (radioGroup.getCheckedRadioButtonId()) {
                                    case -1:
                                        Toast.makeText(getBaseContext(), "Выберите необходимый предмет для начала работы", Toast.LENGTH_LONG).show();
                                        break;
                                    case R.id.radioButton:
                                        intent0.putExtra("subject", "informatics");
                                        intent0.putExtra("num_of_tasks", 23);
                                        item.setChecked(true);
                                        finish();
                                        startActivity(intent0);
                                        break;
                                    case R.id.radioButton2:
                                        intent0.putExtra("subject", "russian");
                                        intent0.putExtra("num_of_tasks", 26);
                                        item.setChecked(true);
                                        finish();
                                        startActivity(intent0);
                                        break;
                                    case R.id.radioButton3:
                                        intent0.putExtra("subject", "maths_base");
                                        intent0.putExtra("num_of_tasks", 20);
                                        item.setChecked(true);
                                        finish();
                                        startActivity(intent0);
                                        break;
                                }
                                break;
                            case R.id.action_mail:
                                text_view.setText("Решать");
                                item.setChecked(true);
                                finish();
                                startActivity(intent1);
                                break;
                            case R.id.action_settings:
                                text_view.setText("Настройки");
                                Intent intent2 = new Intent(getBaseContext(), SettingsActivity.class);
                                startActivity(intent2);
                        }
                        return false;
                    }
                });
    }

    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.button:
                //Intent i;
                //i = new Intent(this, ChooseIntentionActivity.class);
                //startActivity(i);
                RadioGroup radioGroup2 = findViewById(R.id.radioGroup2);
                switch (radioGroup2.getCheckedRadioButtonId()) {
                    case -1:
                        Toast.makeText(getBaseContext(), "Выберите необходимые пункты для начала работы", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioButton4:
                        intent = new Intent(this, ButtonsActivity.class);
                        RadioGroup radioGroup = findViewById(R.id.radioGroup);
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case -1:
                                Toast.makeText(getBaseContext(), "Выберите необходимый предмет для начала работы", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.radioButton:
                                intent.putExtra("subject", "informatics");
                                intent.putExtra("num_of_tasks", 23);
                                startActivity(intent);
                                break;
                            case R.id.radioButton2:
                                intent.putExtra("subject", "russian");
                                intent.putExtra("num_of_tasks", 26);
                                startActivity(intent);
                                break;
                            case R.id.radioButton3:
                                intent.putExtra("subject", "maths_base");
                                intent.putExtra("num_of_tasks", 20);
                                startActivity(intent);
                                break;
                        }
                        break;
                    case R.id.radioButton5:
                        intent = new Intent(this, Test_CreatingActivity.class);
                        radioGroup = findViewById(R.id.radioGroup);
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case -1:
                                Toast.makeText(getBaseContext(), "Выберите необходимый предмет для начала работы", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.radioButton:
                                intent.putExtra("subject", "informatics");
                                intent.putExtra("num_of_tasks", 23);
                                startActivity(intent);
                                break;
                            case R.id.radioButton2:
                                intent.putExtra("subject", "russian");
                                intent.putExtra("num_of_tasks", 26);
                                startActivity(intent);
                                break;
                            case R.id.radioButton3:
                                intent.putExtra("subject", "maths_base");
                                intent.putExtra("num_of_tasks", 20);
                                startActivity(intent);
                                break;
                        }
                        break;
                }
                break;
        }
    }

    /*public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem action_delete_progress = menu.findItem(R.id.action_delete_progress);
        MenuItem action_reload_task = menu.findItem(R.id.action_reload_task);
        MenuItem action_clear_database = menu.findItem(R.id.action_clear_database);

        action_reload_task.setVisible(false);
        action_delete_progress.setVisible(false);
        action_clear_database.setVisible(false);
        return true;
    }*/

    /*public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return true;
        }
    }*/


    @Override
    public void onResume() {
        super.onResume();
        Log.d("myLogs", "Main.resume");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // читаем размер шрифта из EditTextPreference
        String f_Size = prefs.getString(getResources().getString(R.string.pref_size), "14");
        assert f_Size != null;
        float fSize = Float.parseFloat(f_Size);
        // применяем настройки в текстовом поле
        TextView text_view = findViewById(R.id.text_view);
        text_view.setTextSize(TypedValue.COMPLEX_UNIT_SP, fSize);
        //Применяем настройки стиля шрифта
        String regular = prefs.getString(getString(R.string.pref_style), "");
        int typeface = Typeface.NORMAL;
        assert regular != null;
        if (regular.contains("Полужирный"))
            typeface += Typeface.BOLD;
        if (regular.contains("Курсив"))
            typeface += Typeface.ITALIC;
        // меняем настройки в TextView
        text_view.setTypeface(null, typeface);

        String night_mode = prefs.getString("night_mode", "Да");
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
        AppCompatDelegate.setDefaultNightMode(modeNight);
    }
}

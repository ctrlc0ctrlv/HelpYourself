package com.example.examhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

import static com.example.examhelper.AnsweringActivity.APP_PROGRRESS;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("myLogs","OnCreate");
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
            /*case R.id.action_delete_progress:
                final Context context;
                context = MainActivity.this;
                AlertDialog.Builder ad;
                String title = "Вы уверены?";
                String message = "Вы не сможете отменить это действие";
                String yesString = "Да";
                String noString = "Отмена";
                ad = new AlertDialog.Builder(context);
                ad.setTitle(title);  // заголовок
                ad.setMessage(message); // сообщение
                ad.setCancelable(false);
                ad.setPositiveButton(yesString, new DialogInterface.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                    public void onClick(DialogInterface dialog, int arg1) {
                        SharedPreferences activityPreferences = getSharedPreferences(APP_PROGRRESS,Context.MODE_WORLD_WRITEABLE);
                        SharedPreferences.Editor editor = activityPreferences.edit();
                        editor.clear();
                        editor.apply();
                    }
                });
                ad.setNegativeButton(noString, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int arg1) {

                    }
                });
                ad.create();
                ad.show();
                return true;*/
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

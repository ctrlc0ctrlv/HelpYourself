package com.example.examhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class Test_AnsweringActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    TextView textView3;
    TextView textView4;
    TextInputEditText textInputEditText;
    Button enterBtn;
    Button goBtn;
    Bundle arguments;
    AlertDialog.Builder ad;
    String SUBJECT_TABLE_NAME;
    int TASK_NUM;
    private TryingDBHelper tryDBHelper;
    private SQLiteDatabase tryDB;

    public static final String TEST_PROGRESS = "my_test";
    public static final String TEST_PROGRESS_ANSWER = "my_test_answer";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answering);
        //убираем лишнее
        TextView textView2 = findViewById(R.id.textView2);
        textView2.setVisibility(View.GONE);
        //инициализируем компоненты
        textView = findViewById(R.id.textView);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textInputEditText = findViewById(R.id.textInputEditText);
        //инициализируем кнопки
        enterBtn = findViewById(R.id.enterBtn);
        enterBtn.setText("Назад");
        enterBtn.setOnClickListener(this);
        goBtn = findViewById(R.id.goBtn);
        goBtn.setOnClickListener(this);
        //инициализируем переменные
        tryDBHelper = new TryingDBHelper(this);
        arguments = getIntent().getExtras();
        assert arguments != null;
        SUBJECT_TABLE_NAME = arguments.getString("subject");
        TASK_NUM = arguments.getInt("number");
        //уведомление
        final Context context;
        context = Test_AnsweringActivity.this;
        String title = "Вы успешно справились со всеми заданиями теста";
        String message = "Проверить тест?";
        String yesString = "Да";
        String noString = "Нет";
        ad = new AlertDialog.Builder(context);
        ad.setTitle(title);  // заголовок
        ad.setMessage(message); // сообщение
        ad.setCancelable(false);
        ad.setPositiveButton(yesString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                setResult(RESULT_OK);
                finish();
            }
        });
        ad.setNegativeButton(noString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
        //вывод всего нужного на экран
        assert arguments != null;
        int[] base_ids = arguments.getIntArray("base_ids");
        assert base_ids != null;

        textView3.setText("");
        textView3.append(String.valueOf(TASK_NUM));
        textView3.append("/");
        textView3.append(String.valueOf(arguments.getInt("num_of_tasks")));
        textView4.append(" " + Integer.toString(base_ids[TASK_NUM]));
        textView.setText(giveUsl(base_ids[TASK_NUM]));

        SharedPreferences activityPreferences = getSharedPreferences(TEST_PROGRESS, Context.MODE_PRIVATE);
        String previous_answer = activityPreferences.getString(TEST_PROGRESS_ANSWER + "_" + Integer.toString(TASK_NUM), "");
        textInputEditText.setText(previous_answer);
    }

    @Override
    public void onClick(View view) {
        int curr;
        switch (view.getId()) {
            case R.id.goBtn:
                curr = arguments.getInt("number") + 1;
                if (curr <= arguments.getInt("num_of_tasks")) {
                    re_create(curr);
                } else {
                    ad.create();
                    ad.show();
                }
                break;
            case R.id.enterBtn:
                curr = arguments.getInt("number") - 1;
                if (curr > 0) {
                    re_create(curr);
                }
                break;
        }
    }

    void re_create(int n) {
        Intent intent = getIntent();
        intent.putExtra("number", n);

        finish();
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("myLogs", "Создано меню");
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

    public String giveUsl(final int n) {
        //достает из базы данных условие задания с указанным номером
        tryDB = tryDBHelper.getReadableDatabase();
        try {
            tryDBHelper.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String raw = "SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE _id ==" + n;
        Cursor cursor = tryDB.rawQuery(raw, null);
        cursor.moveToFirst();
        String st;
        st = cursor.getString(1);
        cursor.close();
        return st;
    }

    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // читаем размер шрифта из EditTextPreference
        String f_Size = prefs.getString(getResources().getString(R.string.pref_size), "14");
        assert f_Size != null;
        Float fSize = Float.parseFloat(f_Size);
        // применяем настройки в текстовом поле
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fSize);
        textInputEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fSize);
        //Применяем настройки стиля шрифта
        String regular = prefs.getString(getString(R.string.pref_style), "");
        int typeface = Typeface.NORMAL;
        assert regular != null;
        if (regular.contains("Полужирный"))
            typeface += Typeface.BOLD;
        if (regular.contains("Курсив"))
            typeface += Typeface.ITALIC;
        // меняем настройки в TextView
        textView.setTypeface(null, typeface);
    }

    public void onPause() {
        super.onPause();
        Editable ans = textInputEditText.getText();
        assert ans != null;

        SharedPreferences activityPreferences = getSharedPreferences(TEST_PROGRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = activityPreferences.edit();
        ed.putString(TEST_PROGRESS_ANSWER + "_" + Integer.toString(TASK_NUM), ans.toString());
        ed.apply();

        Log.d("myLogs", "текущий ответ = " + ans);
    }
}

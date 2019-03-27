package com.example.examhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;

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

    @Override
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

        tryDB = tryDBHelper.getReadableDatabase();

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
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(DialogInterface dialog, int arg1) {
                set_And_fin();
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

        setUpTable(base_ids[TASK_NUM]);
        setUpWebView(base_ids[TASK_NUM]);

        SharedPreferences activityPreferences = getSharedPreferences(TEST_PROGRESS, Context.MODE_PRIVATE);
        String previous_answer = activityPreferences.getString(TEST_PROGRESS_ANSWER + "_" + Integer.toString(TASK_NUM), "");
        textInputEditText.setText(previous_answer);
    }

    void set_And_fin() {

        finish();
    }

    @Override
    public void onClick(View view) {
        int curr;
        switch (view.getId()) {
            case R.id.goBtn:
                curr = arguments.getInt("number") + 1;
                if (curr <= arguments.getInt("num_of_tasks")) {
                    re_create(curr);
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

    void setUpWebView(int n) {
        if (SUBJECT_TABLE_NAME.equalsIgnoreCase("informatics")) {
            if (TASK_NUM == 15) {
                WebView webView = findViewById(R.id.webView);
                String url = "file:///android_asset/informatics/";
                url += n;
                url += ".jpg";
                webView.loadUrl(url);
            }
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
        ///tryDB = tryDBHelper.getReadableDatabase();
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

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // читаем размер шрифта из EditTextPreference
        String f_Size = prefs.getString(getResources().getString(R.string.pref_size), "14");
        assert f_Size != null;
        float fSize = Float.parseFloat(f_Size);
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

        /*assert arguments != null;
        int[] base_ids = arguments.getIntArray("base_ids");
        assert base_ids != null;
        setUpTable(base_ids[TASK_NUM]);*/
    }

    @Override
    public void onPause() {
        super.onPause();
        Editable ans = textInputEditText.getText();
        assert ans != null;

        SharedPreferences activityPreferences = getSharedPreferences(TEST_PROGRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = activityPreferences.edit();
        ed.putString(TEST_PROGRESS_ANSWER + "_" + Integer.toString(TASK_NUM), ans.toString());
        ed.apply();
    }

    void setUpTable(int n) {
        ArrayList<String> allowed_table = new ArrayList<>();
        allowed_table.add("3");
        allowed_table.add("20");
        allowed_table.add("21");
        String curr = String.valueOf(TASK_NUM);
        Log.d("myLogs", "containing: " + String.valueOf(allowed_table.contains(curr)));
        if ((SUBJECT_TABLE_NAME.equalsIgnoreCase("informatics") && allowed_table.contains(curr)) || (SUBJECT_TABLE_NAME.equalsIgnoreCase("russian") && TASK_NUM == 8)) {
            String raw_table = giveTable(n);
            if (raw_table != null) {
                char height = raw_table.charAt(0);
                char width = raw_table.charAt(2);
                String ids[];
                ids = raw_table.substring(4).split("\\$");
                for (int i = 0; i < ids.length; i++) {
                    ids[i] = ids[i].trim();
                }
                createTable(Integer.parseInt(Character.toString(height)), Integer.parseInt(Character.toString(width)), ids);
            }
        }
    }

    String giveTable(int n) {
        ///tryDB = tryDBHelper.getReadableDatabase();
        String raw = "SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE _id ==" + n;

        String raw_elements;
        Cursor cursor = tryDB.rawQuery(raw, null);
        cursor.moveToFirst();
        raw_elements = cursor.getString(5);
        cursor.close();
        return raw_elements;
    }

    void createTable(int height, int width, String[] ids) {
        TableLayout tableLayout_black = findViewById(R.id.prices_black);
        tableLayout_black.removeAllViews();
        TableLayout tableLayout = findViewById(R.id.prices);
        tableLayout.removeAllViews();
        //общая инициализация
        TableRow tableRow = new TableRow(this);
        TableRow tableRow1 = new TableRow(this);
        TableRow tableRow2 = new TableRow(this);
        TableRow tableRow3 = new TableRow(this);
        TableRow tableRow4 = new TableRow(this);
        TableRow tableRow5 = new TableRow(this);
        TableRow tableRow6 = new TableRow(this);
        TableRow tableRow7 = new TableRow(this);
        TableRow tableRow8 = new TableRow(this);
        TableRow tableRow9 = new TableRow(this);

        TableRow.LayoutParams params = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1);
        params.setMargins(1, 1, 1, 1);
        boolean END_OF_STRING = false;
        int a = 0;
        String night_mode = PreferenceManager.getDefaultSharedPreferences(this).getString("night_mode", "Нет");
        assert night_mode != null;
        for (int i = 1; i <= height; i++) {
            while (a < i * width) {
                if (a >= ids.length) {
                    END_OF_STRING = true;
                    break;
                }
                TextView tableElement = new TextView(this);
                tableElement.setTextSize(20);
                tableElement.setLayoutParams(params);
                tableElement.setGravity(Gravity.START);
                tableElement.setWidth(TableLayout.LayoutParams.MATCH_PARENT);
                if (night_mode.equalsIgnoreCase("Да")) {
                    tableElement.setBackgroundColor(getResources().getColor(R.color.colorTableBlack));
                } else if (night_mode.equalsIgnoreCase("Нет")) {
                    tableElement.setBackgroundColor(getResources().getColor(R.color.colorDefault));
                }
                if (ids[a].equalsIgnoreCase("#")) {
                    tableElement.setText(" ");
                } else {
                    tableElement.setText(ids[a]);
                }
                switch (i) {
                    case 1:
                        tableRow.addView(tableElement);
                        tableRow.setGravity(Gravity.CENTER);
                        break;
                    case 2:
                        tableRow1.addView(tableElement);
                        tableRow1.setGravity(Gravity.CENTER);
                        break;
                    case 3:
                        tableRow2.addView(tableElement);
                        tableRow2.setGravity(Gravity.CENTER);
                        break;
                    case 4:
                        tableRow3.addView(tableElement);
                        tableRow3.setGravity(Gravity.CENTER);
                        break;
                    case 5:
                        tableRow4.addView(tableElement);
                        tableRow4.setGravity(Gravity.CENTER);
                        break;
                    case 6:
                        tableRow5.addView(tableElement);
                        tableRow5.setGravity(Gravity.CENTER);
                        break;
                    case 7:
                        tableRow6.addView(tableElement);
                        tableRow6.setGravity(Gravity.CENTER);
                        break;
                    case 8:
                        tableRow7.addView(tableElement);
                        tableRow7.setGravity(Gravity.CENTER);
                        break;
                    case 9:
                        tableRow8.addView(tableElement);
                        tableRow8.setGravity(Gravity.CENTER);
                        break;
                    case 10:
                        tableRow9.addView(tableElement);
                        tableRow9.setGravity(Gravity.CENTER);
                        break;
                }
                a++;
            }
            //проверка достижения конца строки
            if (END_OF_STRING) {
                break;
            }
        }
        //разное оформление и разные таблицы для разных значений "ночного режима"
        switch (night_mode) {
            case ("Да"):
                //вставка строк в таблицу
                tableLayout_black.bringToFront();
                tableLayout_black.addView(tableRow);
                tableLayout_black.addView(tableRow1);
                tableLayout_black.addView(tableRow2);
                tableLayout_black.addView(tableRow3);
                tableLayout_black.addView(tableRow4);
                tableLayout_black.addView(tableRow5);
                tableLayout_black.addView(tableRow6);
                tableLayout_black.addView(tableRow7);
                tableLayout_black.addView(tableRow8);
                tableLayout_black.addView(tableRow9);
                break;
            case ("Нет"):
                //вставка строк в таблицу
                tableLayout.bringToFront();
                tableLayout.addView(tableRow);
                tableLayout.addView(tableRow1);
                tableLayout.addView(tableRow2);
                tableLayout.addView(tableRow3);
                tableLayout.addView(tableRow4);
                tableLayout.addView(tableRow5);
                tableLayout.addView(tableRow6);
                tableLayout.addView(tableRow7);
                tableLayout.addView(tableRow8);
                tableLayout.addView(tableRow9);
                break;
        }
    }
}

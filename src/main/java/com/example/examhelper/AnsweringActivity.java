package com.example.examhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

@RequiresApi(api = Build.VERSION_CODES.KITKAT)
public class AnsweringActivity extends AppCompatActivity implements View.OnClickListener {
    //настройки (в частности, прогресса)
    public static final String APP_PREFERENCES_PROGRESS_COUNTER = "progress_counter";
    public static final String APP_PREFERENCES_PROGRESS_LVL = "progress_lvl";
    public static final String APP_PREFERENCES_PROGRESS_BASE_NUM = "progress_base_num";
    public static final String APP_PROGRESS = "my_progress";
    public int BASE_NUM = 0;
    public Set<Integer> TASKS = new HashSet<>();
    public Set<Integer> MISTAKES = new HashSet<>();
    //экземпляр класса Task
    Task Task1 = new Task();
    //уведомления
    AlertDialog.Builder ad;
    AlertDialog.Builder ad_delete;
    AlertDialog.Builder ad_reload_task;
    AlertDialog.Builder ad_exception;
    //компоненты разметки экрана
    TextView textView;
    TextView textView2;
    TextView textView3;
    TextView textView4;
    TextInputEditText textInputEditText;
    Button enterBtn;
    Button goBtn;
    //другие часто используемые переменные
    Bundle arguments;
    String SUBJECT_TABLE_NAME;
    //для работы с базами данных
    private TryingDBHelper tryDBHelper;
    private SQLiteDatabase tryDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answering);
        Log.d("myLogs", "Create");
        //инициализация всех компонентов
        textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textInputEditText = findViewById(R.id.textInputEditText);
        enterBtn = findViewById(R.id.enterBtn);
        goBtn = findViewById(R.id.goBtn);
        //инициализация переменных
        arguments = getIntent().getExtras();
        SUBJECT_TABLE_NAME = Objects.requireNonNull(arguments).getString("subject");
        // Обработчики нажатия кнопок
        goBtn.setOnClickListener(this);
        enterBtn.setOnClickListener(this);

        //прописываем уведомление
        final Context context;
        context = AnsweringActivity.this;
        String title = "Вы прошли все уровни в этом задании!";
        String message = "Перейти к следующему заданию или больше практики в этом задании?";
        String yesString = "Дальше";
        String noString = "Больше практики";
        ad = new AlertDialog.Builder(context);
        ad.setTitle(title);  // заголовок
        ad.setMessage(message); // сообщение
        ad.setCancelable(false);
        ad.setPositiveButton(yesString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Intent intent = getIntent();
                int NUM_OF_TASKS = Objects.requireNonNull(getIntent().getExtras()).getInt("num_of_tasks");

                int Level = Task1.getLevel();
                String newLevel = ("Уровень: " + Task1.LevelDown(Level));
                Log.d("myLogs", "new level after getting down is " + Integer.toString(Task1.LevelDown(Level)));
                textView2.setText(newLevel);

                if (GetTaskNum() + 1 <= NUM_OF_TASKS) {
                    Toast.makeText(context, "Вы сделали правильный выбор", Toast.LENGTH_LONG).show();
                    intent.putExtra("number", GetTaskNum() + 1);
                    finish();
                    startActivity(intent);
                } else {
                    Toast.makeText(context, "Увы, это последнее задание. Можете выбрать другое =)", Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        });
        ad.setNegativeButton(noString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Повторение - мать учения", Toast.LENGTH_LONG).show();
                int Level = Task1.getLevel();
                String newLevel = ("Уровень: " + Task1.LevelDown(Level));
                textView2.setText(newLevel);
                textInputEditText.setText("");
                textView.setBackground(textView2.getBackground());
                enterBtn.setEnabled(true);
                setUp();
            }
        });
        //второе уведомление
        String title_delete = "Вы уверены? Это действие невозможно отменить";
        String message_delete = "Ваш прогресс во всех заданиях обнулится";
        String yesString_delete = "Да";
        String noString_delete = "Отмена";
        ad_delete = new AlertDialog.Builder(context);
        ad_delete.setTitle(title_delete);  // заголовок
        ad_delete.setMessage(message_delete); // сообщение
        ad_delete.setCancelable(false);
        ad_delete.setPositiveButton(yesString_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                SharedPreferences activityPreferences = getSharedPreferences(APP_PROGRESS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = activityPreferences.edit();
                editor.clear();
                editor.apply();

                int LVL = activityPreferences.getInt(APP_PREFERENCES_PROGRESS_LVL + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), 1);
                int COUNTER = activityPreferences.getInt(APP_PREFERENCES_PROGRESS_COUNTER + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), 0);

                textView3.setText("");
                textView3.append(Integer.toString(COUNTER));
                textView3.append("/10");
                textView2.setText("");
                textView2.append("Уровень: ");
                textView2.append(Integer.toString(LVL));
                textInputEditText.setText("");
            }
        });
        ad_delete.setNegativeButton(noString_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });

        //третье уведомление
        String message_reload = "Ваш прогресс в этом задании обнулится";
        ad_reload_task = new AlertDialog.Builder(context);
        ad_reload_task.setTitle(title_delete);  // заголовок
        ad_reload_task.setMessage(message_reload); // сообщение
        ad_reload_task.setCancelable(false);
        ad_reload_task.setPositiveButton(yesString_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                SharedPreferences activityPreferences = getSharedPreferences(APP_PROGRESS, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = activityPreferences.edit();

                editor.remove(APP_PREFERENCES_PROGRESS_LVL + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum());
                editor.remove(APP_PREFERENCES_PROGRESS_COUNTER + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum());
                editor.apply();

                int LVL = activityPreferences.getInt(APP_PREFERENCES_PROGRESS_LVL + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), 1);
                int COUNTER = activityPreferences.getInt(APP_PREFERENCES_PROGRESS_COUNTER + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), 0);

                textView3.setText("");
                textView3.append(Integer.toString(COUNTER));
                textView3.append("/10");
                textView2.setText("");
                textView2.append("Уровень: ");
                textView2.append(Integer.toString(LVL));
                textInputEditText.setText("");
            }
        });
        ad_reload_task.setNegativeButton(noString_delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });
        //уведомление об ошибке с базой
        String title_exception = "Ошибка при поиске в базе данных";
        String message_exception = "Если вы видите это сообщение, значит злые силы мешают вам подготовиться к ЕГЭ. Попробуйте еще раз";
        String yesString_exception = "ОК";
        ad_exception = new AlertDialog.Builder(context);
        ad_exception.setTitle(title_exception);  // заголовок
        ad_exception.setMessage(message_exception); // сообщение
        ad_exception.setCancelable(false);
        ad_exception.setPositiveButton(yesString_exception, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                finish();
            }
        });

        //Оформляем задания
        tryDBHelper = new TryingDBHelper(this);

        initArrays();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean saving_progress = prefs.getBoolean("save_progress", true);
        if (saving_progress) {
            SharedPreferences activityPreferences = getSharedPreferences(APP_PROGRESS, Context.MODE_PRIVATE);
            BASE_NUM = activityPreferences.getInt(APP_PREFERENCES_PROGRESS_BASE_NUM + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), 0);
        }
        if (BASE_NUM == 0) {
            setUp();
        } else {
            setUp(BASE_NUM);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        int progress_lvl = Task1.getLevel();
        String sol = textView3.getText().toString();
        String sollutions = sol.replace("/10", "");
        int progress_counter = Integer.parseInt(sollutions);
        outState.putInt("curr_lvl", progress_lvl);
        outState.putInt("curr_solved", progress_counter);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        int lvl = savedInstanceState.getInt("curr_lvl");
        int solved = savedInstanceState.getInt("curr_solved");
        textView2.setText("");
        textView2.append("Уровень: ");
        textView2.append(Integer.toString(lvl));
        textView3.setText("");
        textView3.append(Integer.toString(solved));
        textView3.append("/10");
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

    public void initArrays() {
        //возвращет массив уникальных номеров заданий
        tryDB = tryDBHelper.getReadableDatabase();
        String raw = "SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE number ==" + GetTaskNum();
        switch (Task1.getLevel()) {
            case 1:
                raw += " AND level ==1";
                break;
            case 2:
                raw += " AND (level ==1 OR level ==2)";
                break;
            case 3:
                raw += " AND (level ==1 OR level ==2 OR level ==3)";
                break;
            case 4:
                break;
        }
        Integer[] myArray;
        try {
            Cursor cursor = tryDB.rawQuery(raw, null);
            while (!cursor.isAfterLast()) {
                try {
                    cursor.moveToNext();
                    TASKS.add(cursor.getInt(0));
                } catch (CursorIndexOutOfBoundsException e) {
                    break;
                }
            }
            cursor.close();
        } catch (SQLiteException e) {
            ad_exception.create();
        }
        myArray = TASKS.toArray(new Integer[0]);
        Log.d("myLogs", "Current array=" + Arrays.toString(myArray));

        Integer[] myMistakes = MISTAKES.toArray(new Integer[0]);
        Log.d("myLogs", "Current mistakes = " + Arrays.toString(myMistakes));
    }

    public String giveAns(int n) {
        //возвращает ответ из базы данных по номеру задания
        tryDB = tryDBHelper.getReadableDatabase();
        String raw = "SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE _id ==" + n;
        String ans = "";
        try {
            Cursor cursor = tryDB.rawQuery(raw, null);
            cursor.moveToFirst();
            ans = cursor.getString(2);
            cursor.close();
        } catch (SQLiteException e) {
            ad_exception.create();
        }
        return ans;
    }

    void setUp() {
        //устанавливает условие на экран пользователя
        int n = Task1.getHashNum();
        textView.setText(giveUsl(n));
        textView4.setText("");
        textView4.append(getResources().getString(R.string.current_num));
        textView4.append(" ");
        textView4.append(Integer.toString(n));
        setUpTable(n);
        setUpWebView(n);
        if (n > 0) {
            BASE_NUM = n;
        } else {
            ad_exception.show();
        }
        Log.d("myLogs", "new task num is " + n);
    }

    void setUp(int num) {
        //устанавливает условие на экран пользователя
        textView.setText(giveUsl(num));
        textView4.setText("");
        textView4.append(getResources().getString(R.string.current_num));
        textView4.append(" ");
        textView4.append(Integer.toString(num));
        setUpTable(num);
        setUpWebView(num);
        Task1.setNum(num);
        BASE_NUM = num;
        if (num <= 0) {
            setUp();
        }
    }

    void setUpTable(int n) {
        if (SUBJECT_TABLE_NAME.equalsIgnoreCase("informatics")) {
            String raw_table = giveTable(n);
            if (raw_table != null) {
                char height = raw_table.charAt(0);
                char width = raw_table.charAt(2);
                String ids[];
                ids = raw_table.substring(4).split("\\$");
                createTable(Integer.parseInt(Character.toString(height)), Integer.parseInt(Character.toString(width)), ids);
            }
        }
    }

    void setUpWebView(int n) {
        if (SUBJECT_TABLE_NAME.equalsIgnoreCase("informatics")) {
            if (GetTaskNum() == 15) {
                WebView webView = findViewById(R.id.webView);
                String url = "file:///android_asset/informatics/";
                url += n;
                url += ".png";
                webView.loadUrl(url);
            }
        }
    }

    String giveTable(int n) {
        tryDB = tryDBHelper.getReadableDatabase();
        String raw = "SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE _id ==" + n;

        String raw_elements = "";
        try {
            Cursor cursor = tryDB.rawQuery(raw, null);
            cursor.moveToFirst();
            raw_elements = cursor.getString(5);
            cursor.close();
        } catch (SQLiteException e) {
            ad_exception.create();
        }
        return raw_elements;
    }

    int GetTaskNum() {
        //возвращает номер задания, сохраненный в Extras
        assert arguments != null;
        return arguments.getInt("number");
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        String sol = textView3.getText().toString();
        String sollutions = sol.replace("/10", "");
        int int_solluted = Integer.parseInt(sollutions);
        int int_need_solluted = Integer.parseInt(sol.replace(sollutions + "/", ""));

        switch (view.getId()) {
            case R.id.goBtn:
                enterBtn.setEnabled(true);
                textView.setBackground(textView2.getBackground());
                textInputEditText.setText("");
                setUp();
                break;
            case R.id.enterBtn:
                boolean rez = Task1.Check(Task1.getNum());
                if (rez) {
                    //если задание решено верно
                    int_solluted += 1;
                    sol = int_solluted + "/" + int_need_solluted;
                    if (int_solluted == int_need_solluted) {
                        //если решено необходимое количество для текущего уровня, то повышаем уровень пользователя в этом задании
                        int Level = Task1.getLevel();
                        String newLevel = ("Уровень: " + Task1.LevelUp(Level));
                        textView2.setText(newLevel);
                        sol = "0/" + int_need_solluted;
                        initArrays();
                    }
                    textView3.setText(sol);
                    enterBtn.setEnabled(false);
                }
                break;
        }
    }

    //настроечки размера и стиля шрифта
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.d("myLogs", "Создано меню");
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem action_clear_database = menu.findItem(R.id.action_clear_database);
        action_clear_database.setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Intent intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                Log.d("myLogs", "Выбран пункт меню Настройки");
                return true;
            case R.id.action_delete_progress:
                ad_delete.create();
                ad_delete.show();
                return true;
            case R.id.action_reload_task:
                ad_reload_task.create();
                ad_reload_task.show();
                return true;
            default:
                return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
        Log.d("myLogs", "Resume");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // читаем размер шрифта из EditTextPreference
        Float fSize = Float.parseFloat(Objects.requireNonNull(prefs.getString(getResources().getString(R.string.pref_size), "14")));
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

        boolean saving_progress = prefs.getBoolean("save_progress", true);
        if (saving_progress) {
            SharedPreferences activityPreferences = getSharedPreferences(APP_PROGRESS, Context.MODE_PRIVATE);
            int LVL = activityPreferences.getInt(APP_PREFERENCES_PROGRESS_LVL + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), 1);
            int COUNTER = activityPreferences.getInt(APP_PREFERENCES_PROGRESS_COUNTER + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), 0);
            int BASE_NUM = activityPreferences.getInt(APP_PREFERENCES_PROGRESS_BASE_NUM + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), 1);

            textView3.setText("");
            textView3.append(Integer.toString(COUNTER));
            textView3.append("/10");
            textView2.setText("");
            textView2.append("Уровень: ");
            textView2.append(Integer.toString(LVL));
            if (BASE_NUM <= 0) {
                textView4.setText("");
                textView4.append("Номер задания: ");
                textView4.append(Integer.toString(BASE_NUM));
            }
        }
        //перерисуем таблицу (в основном ориентировано на изменение цветовой схемы/ночного режима)
        if (BASE_NUM == 0) {
            setUp();
        } else {
            setUp(BASE_NUM);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("myLogs", "Pause");
        //Применяем настройку сохранения прогресса
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean saving_progress = prefs.getBoolean("save_progress", true);

        if (saving_progress) {
            SharedPreferences activityPreferences = getSharedPreferences(APP_PROGRESS, Context.MODE_PRIVATE);

            String sol = textView3.getText().toString();
            String sollutions = sol.replace("/10", "");
            int int_solluted = Integer.parseInt(sollutions);

            BASE_NUM = Integer.parseInt(textView4.getText().toString().replace("Номер задания: ", ""));

            SharedPreferences.Editor ed = activityPreferences.edit();
            ed.putInt(APP_PREFERENCES_PROGRESS_LVL + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), Task1.getLevel());
            Log.d("myLogs", "onPause: current lvl is " + Integer.toString(Task1.getLevel()));
            ed.putInt(APP_PREFERENCES_PROGRESS_COUNTER + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), int_solluted);
            ed.putInt(APP_PREFERENCES_PROGRESS_BASE_NUM + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), BASE_NUM);
            ed.apply();
        }
    }

    void createTable(int height, int width, String[] ids) {
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
        String night_mode = PreferenceManager.getDefaultSharedPreferences(this).getString("night_mode", "Включать автоматически");
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
                if (night_mode.equalsIgnoreCase("Да")) {
                    tableElement.setBackgroundColor(getResources().getColor(R.color.colorDefultBlack));
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
                        tableRow1.setGravity(Gravity.START);
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
            case ("Включать автоматически"):
                TableLayout tableLayout_auto = findViewById(R.id.prices_auto);
                tableLayout_auto.removeAllViews();
                tableLayout_auto.bringToFront();

                tableLayout_auto.addView(tableRow);
                tableLayout_auto.addView(tableRow1);
                tableLayout_auto.addView(tableRow2);
                tableLayout_auto.addView(tableRow3);
                tableLayout_auto.addView(tableRow4);
                tableLayout_auto.addView(tableRow5);
                tableLayout_auto.addView(tableRow6);
                tableLayout_auto.addView(tableRow7);
                tableLayout_auto.addView(tableRow8);
                tableLayout_auto.addView(tableRow9);
                break;
            case ("Да"):
                //вставка строк в таблицу
                TableLayout tableLayout_black = findViewById(R.id.prices_black);
                tableLayout_black.removeAllViews();
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
                TableLayout tableLayout = findViewById(R.id.prices);
                tableLayout.removeAllViews();
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

    public class Task {
        //переменная для хранения номера задания
        private int num;

        int getNum() {
            //возвращает номер текущего задания
            return num;
        }

        void setNum(int x) {
            //устанавливает номер текущего задания
            this.num = x;
        }

        int getHashNum() {
            //возвращает номер задания на основании текущей выборки и ошибок
            //С функцией рандома нужно категорически поколдовать, потому что какой-то он не шибко рандомный
            int x;
            Integer[] tasks = TASKS.toArray(new Integer[0]);
            Integer[] mistakes = MISTAKES.toArray(new Integer[0]);

            Random random = new Random();
            boolean not_mistakes = random.nextBoolean();
            if (mistakes.length > 0) {
                if (not_mistakes) {
                    x = tasks[random.nextInt(tasks.length)];
                } else {
                    x = mistakes[random.nextInt(mistakes.length)];
                }
            } else {
                x = tasks[random.nextInt(tasks.length)];
            }
            this.setNum(x);
            return x;
        }

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        boolean Check(int n) {
            //проверка текущего задания
            boolean rez;
            if (giveAns(n).equalsIgnoreCase(Objects.requireNonNull(textInputEditText.getText()).toString())) {
                textView.setBackgroundResource(R.color.colorAccept);
                rez = true;
                MISTAKES.remove(n);
            } else {
                textView.setBackgroundResource(R.color.colorDeny);
                rez = false;
                MISTAKES.add(n);
            }
            return rez;
        }

        int getLevel() {
            //возвращает текущий уровень
            int Level;
            Level = Integer.parseInt(textView2.getText().toString().replace("Уровень: ", ""));
            return (Level);
        }

        int LevelUp(int Level) {
            //увеличивает уровень на 1 (если возможно)
            int LevelEquals = Level;
            if (LevelEquals <= 3) {
                LevelEquals += 1;
            }
            if (LevelEquals == 4) {
                ad.create();
                ad.show();
            }
            return (LevelEquals);
        }

        int LevelDown(int Level) {
            //уменьшает уровень на 1 (если возможно)
            int LevelEquals = Level;
            if (LevelEquals >= 2) {
                LevelEquals -= 1;
            }
            return (LevelEquals);
        }
    }
}
package com.easyege.examhelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.easyege.examhelper.data.CustomDbHelper;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class AnsweringActivity extends AppCompatActivity implements View.OnClickListener {
    //настройки (в частности, прогресса)
    private static final String APP_PREFERENCES_PROGRESS_COUNTER = "progress_counter";
    private static final String APP_PREFERENCES_PROGRESS_LVL = "progress_lvl";
    private static final String APP_PREFERENCES_PROGRESS_BASE_NUM = "progress_base_num";
    private static final String APP_PROGRESS = "my_progress";
    private final Set<Integer> TASKS = new HashSet<>();
    private final Set<Integer> MISTAKES = new HashSet<>();
    //экземпляр класса Task
    private final Task Task1 = new Task();
    private int BASE_NUM = 0;
    private String Url;
    //уведомления
    private AlertDialog.Builder ad;
    private AlertDialog.Builder ad_delete;
    private AlertDialog.Builder ad_reload_task;
    private AlertDialog.Builder ad_exception;
    //компоненты разметки экрана
    private WebView webView;
    //TextView textView;
    private TextView textView2;
    private TextView textView3;
    private TextView textView4;
    private TextInputEditText textInputEditText;
    private Button enterBtn;
    //другие часто используемые переменные
    private Bundle arguments;
    private String SUBJECT_TABLE_NAME;
    private SQLiteDatabase tryDB;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answering);
        Log.d("myLogs", "Create AnsweringActivity");
        //инициализация всех компонентов
        webView = findViewById(R.id.webView);

        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setLoadWithOverviewMode(true);
        //webSettings.setUseWideViewPort(true);
        //webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setSupportZoom(true);
        webSettings.setDefaultTextEncodingName("utf-8");

        webView.setBackgroundColor(getResources().getColor(R.color.newDefault));
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String night = sharedPreferences.getString("night_mode", "Нет");
        assert night != null;
        String nightUrl = "</style>" + "<script type='text/x-mathjax-config'>" + " MathJax.Hub.Config({" + " showMathMenu: false," + " jax: ['input/TeX','output/HTML-CSS']," + " extensions: ['tex2jax.js','MathMenu.js','MathZoom.js']," + " tex2jax: { inlineMath: [ ['$','$'] ], processEscapes: true }," + " TeX: {" + " extensions:['AMSmath.js','AMSsymbols.js'," + " 'noUndefined.js']" + " }" + " });" + "</script>" + "<script type='text/javascript' src='file:///android_asset/MathJax/MathJax.js'>" + "</script>" + "<p style=\"line-height:1,5; padding: 0 0; font-size: 16px; color: white\" align=\"justify\">" + "<span >";
        String dayUrl = "</style>" + "<script type='text/x-mathjax-config'>" + " MathJax.Hub.Config({" + " showMathMenu: false," + " jax: ['input/TeX','output/HTML-CSS']," + " extensions: ['tex2jax.js','MathMenu.js','MathZoom.js']," + " tex2jax: { inlineMath: [ ['$','$'] ], processEscapes: true }," + " TeX: {" + " extensions:['AMSmath.js','AMSsymbols.js'," + " 'noUndefined.js']" + " }" + " });" + "</script>" + "<script type='text/javascript' src='file:///android_asset/MathJax/MathJax.js'>" + "</script>" + "<p style=\"line-height:1,5; padding: 0 0; font-size: 16px\" align=\"justify\">" + "<span >";
        switch (night) {
            case ("Да"):
                Url = nightUrl;
                break;
            case ("Нет"):
                Url = dayUrl;
                break;
        }

        /*// Demo display equation url += "This is a display equation: $$P=\frac{F}{A}$$";
        String url = Url + "This is also an identical display equation with different format:\\[P=\\frac{F}{A+B}\\]";
        // equations aligned at equal sign url += "You can also put aligned equations just like Latex:";
        String align = "\\begin{aligned}" + "F\\; &= P \\times (A+B) = 4000 \\times 0.2 = 800\\; \\text{N}\\end{aligned}";
        align += "Так же работает русский текст";
        align += "\\begin{aligned} A_{2} + B_{2} = C_{1} \\end{aligned}";
        align += "\\begin{aligned} \\frac{(a+b)^3}{4} - \\frac{(a-b)^2}{4} = ab \\end{aligned}";
        align += "\\begin{aligned} _{n}^{m}X + _{k}^{l}Y \\to \\dots \\end{aligned}";
        align += "\\begin{aligned} \\text{Можно делать так: }(2x + \\frac{1}{3x})^2 \\text{ или даже так: } \\left( 2x + \\frac{1}{3x} \\right)^2 \\end{aligned}";
        align += "\\begin{aligned} \\sum_{i=0}^n i^2 = \\frac{(n^2+n)(2n+1)}{6} \\end{aligned}";
        url += align;
        // Finally, must enclose the brackets url += "</span></p>";
        webView.loadDataWithBaseURL("http://bar", url, "text/html", "utf-8", "");*/

        //textView = findViewById(R.id.textView);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textInputEditText = findViewById(R.id.textInputEditText);
        enterBtn = findViewById(R.id.enterBtn);
        Button goBtn = findViewById(R.id.goBtn);
        //инициализация переменных
        arguments = getIntent().getExtras();
        assert arguments != null;
        SUBJECT_TABLE_NAME = arguments.getString("subject");
        // Обработчики нажатия кнопок
        goBtn.setOnClickListener(this);
        enterBtn.setOnClickListener(this);

        /*
         * Диалоговое окно номер 1
         * */
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
                int NUM_OF_TASKS = arguments.getInt("num_of_tasks");

                int Level = Task1.getLevel();
                String newLevel = ("Уровень: " + Task1.LevelDown(Level));
                Log.d("myLogs", "new level after getting down is " + Task1.LevelDown(Level));
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
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Повторение - мать учения", Toast.LENGTH_LONG).show();
                int Level = Task1.getLevel();
                String newLevel = ("Уровень: " + Task1.LevelDown(Level));
                textView2.setText(newLevel);
                textInputEditText.setText("");
                //textView.setBackground(textView2.getBackground());
                enterBtn.setEnabled(true);
                setUp();
            }
        });
        /*
         * Диалоговое окно номер 2
         * */
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

        /*
         * Диалоговое окно номер 3
         */
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
        /*
         * Диалог об ошибке
         */
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
        //для работы с базами данных
        TryingDBHelper tryDBHelper = new TryingDBHelper(this);
        /*try {
            tryDBHelper.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //tryDB = tryDBHelper.getReadableDatabase();
        tryDB = tryDBHelper.getWritableDatabase();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean saving_progress = prefs.getBoolean("save_progress", true);
        if (saving_progress) {
            SharedPreferences activityPreferences = getSharedPreferences(APP_PROGRESS, Context.MODE_PRIVATE);
            BASE_NUM = activityPreferences.getInt(APP_PREFERENCES_PROGRESS_BASE_NUM + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), 0);
        }
        /*if (BASE_NUM == 0) {
            setUp();
        } else {
            setUp(BASE_NUM);
        }*/
        //tryDB.close();

        insertRecords();

        initArrays();
    }

    private void insertRecords() {
        CustomDbHelper cDBHelper = new CustomDbHelper(this);
        SQLiteDatabase cDb = cDBHelper.getReadableDatabase();
        cDBHelper.onCreate(cDb);

        Cursor ccursor = cDb.rawQuery("SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE level ==1", null);
        Cursor cursor = null;
        boolean err = false;
        try {
            cursor = tryDB.rawQuery("SELECT * FROM " + SUBJECT_TABLE_NAME, null);
        } catch (SQLiteException e) {
            Log.d("myLogs", e.toString());
            err = true;
        }

        if (err) {
            Toast.makeText(this, "База данных копируется, попробуйте еще раз", Toast.LENGTH_LONG).show();
            finish();
        }

        String sql;

        if (SUBJECT_TABLE_NAME.equalsIgnoreCase("maths_base")) {
            sql = "INSERT INTO " + SUBJECT_TABLE_NAME + " VALUES(?,?,?,?,?)";
        } else {
            sql = "INSERT INTO " + SUBJECT_TABLE_NAME + " VALUES(?,?,?,?,?,?)";
        }

        SQLiteStatement statement = null;
        int i = 0;
        int count = 0;


        try {
            statement = tryDB.compileStatement(sql);
            assert cursor != null;
            cursor.moveToLast();
            i = cursor.getInt(0) + 1;
            cursor.close();
        } catch (SQLiteException e) {
            Log.d("myLogs", e.toString());
        }

        assert statement != null;

        tryDB.beginTransaction();
        try {
            ccursor.moveToFirst();
            while (!ccursor.isAfterLast()) {
                statement.clearBindings();
                statement.bindLong(1, i);
                statement.bindString(2, ccursor.getString(1));
                statement.bindString(3, ccursor.getString(2));
                statement.bindLong(4, ccursor.getInt(3));
                statement.bindLong(5, ccursor.getInt(4));
                if (!SUBJECT_TABLE_NAME.equalsIgnoreCase("maths_base")) {
                    statement.bindString(6, "");
                }
                statement.execute();
                Log.d("myLogs", "row " + i);
                i += 1;
                count += 1;
                ccursor.moveToNext();
            }
            tryDB.setTransactionSuccessful();
            Log.d("myLogs", "transaction success: " + count + " rows copied");
        } finally {
            tryDB.endTransaction();
        }
        ccursor.close();
        cDb.close();
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

    private String giveUsl(final int n) {
        //достает из базы данных условие задания с указанным номером
        //tryDB = tryDBHelper.getReadableDatabase();
        /*try {
            tryDBHelper.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        String raw = "SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE _id ==" + n;
        Cursor cursor = tryDB.rawQuery(raw, null);
        cursor.moveToFirst();
        String st;
        st = cursor.getString(1);
        cursor.close();
        //tryDB.close();
        return st;
    }

    private void initArrays() {
        //возвращет массив уникальных номеров заданий
        /*try {
            tryDBHelper.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        //tryDB = tryDBHelper.getReadableDatabase();
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
        //Integer[] myArray = TASKS.toArray(new Integer[0]);
        //Log.d("myLogs", "Current array=" + Arrays.toString(myArray));

        //Integer[] myMistakes = MISTAKES.toArray(new Integer[0]);
        //Log.d("myLogs", "Current mistakes = " + Arrays.toString(myMistakes));
        //tryDB.close();
    }

    private String[] giveAns(int n) {
        //возвращает ответ из базы данных по номеру задания
        //tryDB = tryDBHelper.getReadableDatabase();
        String raw = "SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE _id ==" + n;
        String[] ans = null;
        try {
            Cursor cursor = tryDB.rawQuery(raw, null);
            cursor.moveToFirst();
            ans = cursor.getString(2).split("\\$");
            cursor.close();
        } catch (SQLiteException e) {
            ad_exception.create();
        }
        //tryDB.close();
        return ans;
    }

    private void setUp() {
        //устанавливает условие на экран пользователя
        int n = Task1.getHashNum();
        String url = Url + giveUsl(n).replace("\n", "<br/>");
        webView.loadDataWithBaseURL("http://bar", url, "text/html", "utf-8", "");
        //textView.setText(giveUsl(n));
        textView4.setText("");
        textView4.append(getResources().getString(R.string.current_num));
        textView4.append(" ");
        textView4.append(Integer.toString(n));
        setUpTable(n);
        Log.d("myLogs", "1");
        setUpWebView(n);
        if (n > 0) {
            BASE_NUM = n;
        } else {
            ad_exception.show();
        }
    }

    private void setUp(int num) {
        //устанавливает условие на экран пользователя
        String url = Url + giveUsl(num).replace("\n", "<br/>");
        webView.loadDataWithBaseURL("http://bar", url, "text/html", "utf-8", "");
        //textView.setText(giveUsl(num));
        textView4.setText("");
        textView4.append(getResources().getString(R.string.current_num));
        textView4.append(" ");
        textView4.append(Integer.toString(num));
        setUpTable(num);
        Log.d("myLogs", "2");
        setUpWebView(num);
        Task1.setNum(num);
        BASE_NUM = num;
        if (num <= 0) {
            setUp();
        }
    }

    private void setUpTable(int n) {
        ArrayList<String> allowed_table = new ArrayList<>();
        allowed_table.add("3");
        allowed_table.add("8");
        allowed_table.add("17");
        allowed_table.add("19");
        allowed_table.add("20");
        allowed_table.add("21");
        String curr = String.valueOf(GetTaskNum());
        Log.d("myLogs", "containing: " + allowed_table.contains(curr));
        TableLayout tableLayout_black = findViewById(R.id.prices_black);
        tableLayout_black.removeAllViews();
        TableLayout tableLayout = findViewById(R.id.prices);
        tableLayout.removeAllViews();
        if ((SUBJECT_TABLE_NAME.equalsIgnoreCase("informatics") && allowed_table.contains(curr)) || (SUBJECT_TABLE_NAME.equalsIgnoreCase("russian") && GetTaskNum() == 8)) {
            String raw_table = giveTable(n);
            if (raw_table != null) {
                Log.d("myLogs", raw_table);
                char height = raw_table.charAt(0);
                char width = raw_table.charAt(2);
                String[] ids;
                ids = raw_table.substring(4).split("\\$");
                for (int i = 0; i < ids.length; i++) {
                    ids[i] = ids[i].trim();
                }
                createTable(Integer.parseInt(Character.toString(height)), Integer.parseInt(Character.toString(width)), ids);
            }
        }
    }

    private void setUpWebView(int n) {
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(0);
        imageView.setMinimumHeight(0);


        if (SUBJECT_TABLE_NAME.equalsIgnoreCase("informatics")) {
            if (GetTaskNum() == 15 || GetTaskNum() == 3 && (n == 430 || n == 431 || n == 432) || GetTaskNum() == 12 && (n == 451 || n == 452 || n == 453 || n == 454)) {
                /*String url = "file:///android_asset/informatics/";
                url += String.valueOf(n);
                url += ".jpg";
                //webView.loadUrl(url);
                //imageView.setImageURI(Uri.parse(url));
                //imageView.setImageResource(R.drawable.i_have_done);*/

                try {
                    // получаем входной поток
                    String uri = "informatics/";
                    uri += String.valueOf(n);
                    uri += ".jpg";
                    InputStream ims = getAssets().open(uri);
                    // загружаем как Drawable
                    Drawable d = Drawable.createFromStream(ims, null);
                    // выводим картинку в ImageView
                    imageView.setImageDrawable(d);
                    imageView.setMinimumHeight(500);
                } catch (IOException ex) {
                    Log.d("myLogs", "EXCEPTION");
                }
            }
        }
    }

    private String giveTable(int n) {
        //tryDB = tryDBHelper.getReadableDatabase();
        String raw = "SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE _id ==" + n;

        String raw_elements = null;
        try {
            Cursor cursor = tryDB.rawQuery(raw, null);
            cursor.moveToFirst();
            raw_elements = cursor.getString(5);
            if (raw_elements == null) {
                Log.d("myLogs", "empty");
            } else if (raw_elements.equals("")) {
                Log.d("myLogs", "zero length string at position " + n);
            }
            cursor.close();
        } catch (SQLiteException e) {
            ad_exception.create();
        }
        assert raw_elements != null;
        raw_elements = raw_elements.trim();
        //tryDB.close();
        return raw_elements;
    }

    private int GetTaskNum() {
        //возвращает номер задания, сохраненный в Extras
        return arguments.getInt("number");
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        String sol = textView3.getText().toString();
        String sollutions = sol.replace("/10", "");
        int int_solluted = Integer.parseInt(sollutions);
        int int_need_solluted = Integer.parseInt(sol.replace(sollutions + "/", ""));

        switch (view.getId()) {
            case R.id.goBtn:
                enterBtn.setEnabled(true);
                webView.setBackgroundColor(getResources().getColor(R.color.newDefault));
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
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem action_clear_database = menu.findItem(R.id.action_clear_database);
        MenuItem settings = menu.findItem(R.id.action_settings);
        action_clear_database.setVisible(false);
        settings.setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // читаем размер шрифта из EditTextPreference
        String f_Size = prefs.getString("text_size", "14");
        assert f_Size != null;
        //float fSize = Float.parseFloat(f_Size);
        // применяем настройки в текстовом поле
        //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fSize);
        //textInputEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fSize);
        // меняем настройки в TextView
        //textView.setTypeface(null, typeface);

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
                textView4.append(getResources().getString(R.string.current_num));
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
        //Применяем настройку сохранения прогресса
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean saving_progress = prefs.getBoolean("save_progress", true);

        if (saving_progress) {
            SharedPreferences activityPreferences = getSharedPreferences(APP_PROGRESS, Context.MODE_PRIVATE);

            String sol = textView3.getText().toString();
            String sollutions = sol.replace("/10", "");
            int int_solluted = Integer.parseInt(sollutions);

            BASE_NUM = Integer.parseInt(textView4.getText().toString().replace(getResources().getString(R.string.current_num) + " ", ""));

            SharedPreferences.Editor ed = activityPreferences.edit();
            ed.putInt(APP_PREFERENCES_PROGRESS_LVL + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), Task1.getLevel());
            ed.putInt(APP_PREFERENCES_PROGRESS_COUNTER + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), int_solluted);
            ed.putInt(APP_PREFERENCES_PROGRESS_BASE_NUM + "_" + SUBJECT_TABLE_NAME + "_" + GetTaskNum(), BASE_NUM);
            ed.apply();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tryDB.close();
    }

    private void createTable(int height, int width, String[] ids) {
        //TableLayout tableLayout_auto = findViewById(R.id.prices_auto);
        //tableLayout_auto.removeAllViews();
        TableLayout tableLayout_black = findViewById(R.id.prices_black);
        //tableLayout_black.removeAllViews();
        TableLayout tableLayout = findViewById(R.id.prices);
        //tableLayout.removeAllViews();
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
                    tableElement.setBackgroundColor(getResources().getColor(R.color.newDefault));
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

    private class Task {
        //переменная для хранения номера задания
        private int num;

        private int getNum() {
            //возвращает номер текущего задания
            return num;
        }

        private void setNum(int x) {
            //устанавливает номер текущего задания
            this.num = x;
        }

        private int getHashNum() {
            /*
             * Возвращает числовое значение номера задания на основании текущей выборки заданий и массива ошибок
             */
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

        private boolean Check(int n) {
            /*
             * Проверяет задание; возвращает true - задание решено верно или false - задание решено неверно
             */
            boolean rez;
            Editable gotText = textInputEditText.getText();
            assert gotText != null;
            if (Arrays.asList(giveAns(n)).contains(gotText.toString())) {
                webView.setBackgroundColor(getResources().getColor(R.color.colorAccept));
                rez = true;
                MISTAKES.remove(n);
            } else {
                webView.setBackgroundColor(getResources().getColor(R.color.colorDeny));
                rez = false;
                MISTAKES.add(n);
            }
            return rez;
        }

        private int getLevel() {
            /*
             * Возвращает числовое значение текущего уровня
             */
            int Level;
            Level = Integer.parseInt(textView2.getText().toString().replace("Уровень: ", ""));
            return (Level);
        }

        private int LevelUp(int Level) {
            /*
             * Увеличивает уровень на 1 (если это возможно)
             */
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

        private int LevelDown(int Level) {
            /*
             * Уменьшает уровень на 1 (если это возможно)
             */
            int LevelEquals = Level;
            if (LevelEquals >= 2) {
                LevelEquals -= 1;
            }
            return (LevelEquals);
        }
    }
}
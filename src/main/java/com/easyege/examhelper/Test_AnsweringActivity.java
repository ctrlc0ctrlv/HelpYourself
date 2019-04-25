package com.easyege.examhelper;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class Test_AnsweringActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TEST_PROGRESS = "my_test";
    private static final String TEST_PROGRESS_ANSWER = "my_test_answer";
    private String Url;
    private WebView webView;
    //TextView textView;
    private TextView textView3;
    private TextView textView4;
    private TextInputEditText textInputEditText;
    private Bundle arguments;
    private AlertDialog.Builder ad;
    private String SUBJECT_TABLE_NAME;
    private int TASK_NUM;
    private int[] base_ids;
    private SQLiteDatabase tryDB;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answering);
        //убираем лишнее
        TextView textView2 = findViewById(R.id.textView2);
        textView2.setVisibility(View.GONE);
        //инициализируем компоненты
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

        //webView.getSettings().setJavaScriptEnabled(true);
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

        //textView = findViewById(R.id.textView);
        textView3 = findViewById(R.id.textView3);
        textView4 = findViewById(R.id.textView4);
        textInputEditText = findViewById(R.id.textInputEditText);
        //инициализируем кнопки
        Button enterBtn = findViewById(R.id.enterBtn);
        enterBtn.setText("Назад");
        enterBtn.setOnClickListener(this);
        Button goBtn = findViewById(R.id.goBtn);
        goBtn.setOnClickListener(this);
        //инициализируем переменные
        TryingDBHelper tryDBHelper = new TryingDBHelper(this);

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
        base_ids = arguments.getIntArray("base_ids");
        assert base_ids != null;

        setUp();
    }

    private void set_And_fin() {
        setResult(666);
        //Log.d("myLogs","result set successfully");
        finish();
    }

    private void setUp() {
        textView3.setText("");
        textView3.append(String.valueOf(TASK_NUM));
        textView3.append("/");
        textView3.append(String.valueOf(arguments.getInt("num_of_tasks")));

        textView4.setText(getResources().getString(R.string.current_num));
        textView4.append(" " + base_ids[TASK_NUM]);
        //textView.setText(giveUsl(base_ids[TASK_NUM]));
        final String _url = Url + giveUsl(base_ids[TASK_NUM]).replace("\n", "<br/>");
        webView.loadDataWithBaseURL("http://bar", _url, "text/html", "utf-8", "");
        /*webView.setWebViewClient(new WebViewClient() {
            @Override public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                if (!url.startsWith("http://bar")) return;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                    view.loadUrl(_url);
                } else { view.evaluateJavascript(_url, null); } } });*/

        setUpTable(base_ids[TASK_NUM]);
        setUpWebView(base_ids[TASK_NUM]);

        SharedPreferences activityPreferences = getSharedPreferences(TEST_PROGRESS, Context.MODE_PRIVATE);
        String previous_answer = activityPreferences.getString(TEST_PROGRESS_ANSWER + "_" + TASK_NUM, "");
        textInputEditText.setText(previous_answer);
        //Log.d("myLogs","current TASK_NUM value is "+String.valueOf(TASK_NUM));
    }

    @Override
    public void onClick(View view) {
        int curr;
        switch (view.getId()) {
            case R.id.goBtn:
                curr = TASK_NUM + 1;
                TASK_NUM += 1;
                if (curr <= arguments.getInt("num_of_tasks")) {
                    re_create(1);
                } else if (curr == arguments.getInt("num_of_tasks") + 1) {
                    ad.create();
                    ad.show();
                }
                break;
            case R.id.enterBtn:
                curr = TASK_NUM - 1;
                TASK_NUM -= 1;
                if (curr > 0) {
                    re_create(-1);
                }
                break;
        }
    }

    private void setUpWebView(int n) {
        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageResource(0);
        imageView.setMinimumHeight(0);

        int[] allowed_picture = new int[]{185, 186, 187, 190, 191, 192, 193, 194, 195, 196, 197, 198, 199, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211, 212, 213, 283, 284, 294, 430, 431, 432, 451, 452, 453, 454, 481, 482, 483, 484, 485, 486, 487, 488, 489, 490, 491, 492, 493, 494, 495};
        ArrayList<Integer> pic = new ArrayList<>();
        for (int a : allowed_picture) {
            pic.add(a);
        }

        if (SUBJECT_TABLE_NAME.equalsIgnoreCase("informatics") || SUBJECT_TABLE_NAME.equalsIgnoreCase("maths_base")) {
            if ((TASK_NUM == 13 || TASK_NUM == 14 || TASK_NUM == 15 || TASK_NUM == 3 || TASK_NUM == 12) && pic.contains(n)) {
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

    private void re_create(int n) {
        Editable ans = textInputEditText.getText();
        assert ans != null;

        SharedPreferences activityPreferences = getSharedPreferences(TEST_PROGRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = activityPreferences.edit();
        ed.putString(TEST_PROGRESS_ANSWER + "_" + (TASK_NUM - n), ans.toString());
        //Log.d("myLogs","preferences: written for "+String.valueOf(TASK_NUM-n));
        ed.apply();
        setUp();
    }

    private String giveUsl(final int n) {
        //достает из базы данных условие задания с указанным номером
        ///tryDB = tryDBHelper.getReadableDatabase();
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
        return st;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // читаем размер шрифта из EditTextPreference
        //String f_Size = prefs.getString(getResources().getString(R.string.pref_size), "14");
        //assert f_Size != null;
        //float fSize = Float.parseFloat(f_Size);
        // применяем настройки в текстовом поле
        //textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, fSize);
        //textInputEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP, fSize);
        //textView.setTypeface(null, typeface);

        /*assert arguments != null;
        int[] base_ids = arguments.getIntArray("base_ids");
        assert base_ids != null;
        setUpTable(base_ids[TASK_NUM]);*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        Editable ans = textInputEditText.getText();
        assert ans != null;

        SharedPreferences activityPreferences = getSharedPreferences(TEST_PROGRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = activityPreferences.edit();
        ed.putString(TEST_PROGRESS_ANSWER + "_" + TASK_NUM, ans.toString());
        ed.apply();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tryDB.close();
    }

    private void setUpTable(int n) {
        ArrayList<String> allowed_table = new ArrayList<>();
        allowed_table.add("3");
        allowed_table.add("8");
        allowed_table.add("17");
        allowed_table.add("19");
        allowed_table.add("20");
        allowed_table.add("21");
        String curr = String.valueOf(TASK_NUM);
        Log.d("myLogs", "containing: " + allowed_table.contains(curr));
        TableLayout tableLayout_black = findViewById(R.id.prices_black);
        tableLayout_black.removeAllViews();
        TableLayout tableLayout = findViewById(R.id.prices);
        tableLayout.removeAllViews();
        if ((SUBJECT_TABLE_NAME.equalsIgnoreCase("informatics") && allowed_table.contains(curr)) || (SUBJECT_TABLE_NAME.equalsIgnoreCase("russian") && TASK_NUM == 8)) {
            String raw_table = giveTable(n);
            if (raw_table != null) {
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

    private String giveTable(int n) {
        ///tryDB = tryDBHelper.getReadableDatabase();
        String raw = "SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE _id ==" + n;

        String raw_elements;
        Cursor cursor = tryDB.rawQuery(raw, null);
        cursor.moveToFirst();
        raw_elements = cursor.getString(5);
        cursor.close();
        return raw_elements;
    }

    private void createTable(int height, int width, String[] ids) {
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
}

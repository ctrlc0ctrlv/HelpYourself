package com.example.examhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;
import java.util.Random;

public class AnsweringActivity extends AppCompatActivity implements View.OnClickListener {
    private DefaultTasksDBHelper mDbHelper;
    private SQLiteDatabase mDb;
    Task Task1 = new Task();
    AlertDialog.Builder ad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answering);
        // Обработчики нажатия кнопок
        Button enterBtn = findViewById(R.id.enterBtn);
        Button goBtn = findViewById(R.id.goBtn);
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
        ad.setPositiveButton(yesString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Вы сделали правильный выбор", Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                intent.putExtra("number", GetTaskNum()+1);
                finish();
                startActivity(intent);
            }
        });
        ad.setNegativeButton(noString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Повторение - мать учения", Toast.LENGTH_LONG).show();
            }
        });
        ad.setCancelable(false);
        //Оформляем задания
        mDbHelper = new DefaultTasksDBHelper(this);
        setUp();
    }

    public String giveUsl(int n){
        mDb = mDbHelper.getReadableDatabase();
        String raw = "SELECT * FROM informatics WHERE number =="+GetTaskNum();
        
        switch (Task1.getLevel()){
            case 1:
                raw += " AND level ==1";
                break;
            case 2:
                ad.show();
                //raw += " AND (level ==1 OR level ==2)";
                break;
            case 3:
                raw += " AND (level ==1 OR level ==2 OR level ==3)";
                break;
            case 4:
                ad.show();
                break;
        }
        
        Cursor cursor = mDb.rawQuery(raw, null);
        cursor.moveToPosition(n);
        String st = cursor.getString(1);
        cursor.close();
        return st;
    }

    public String giveAns(int n){
        mDb = mDbHelper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery("SELECT * FROM informatics", null);
        cursor.moveToPosition(n);
        String ans = cursor.getString(2);
        cursor.close();
        return ans;
    }

    void setUp(){
        int n = Task1.getNewNum();
        TextView textView = findViewById(R.id.textView);
        TextView textView4 = findViewById(R.id.textView4);
        textView.setText(giveUsl(n));
        textView4.setText(getResources().getString(R.string.current_num) +" "+ Integer.toString(n+1));
    }

    Integer GetTaskNum(){
        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        return arguments.getInt("number");
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        Button enterBtn = findViewById(R.id.enterBtn);
        Button goBtn = findViewById(R.id.goBtn);
        TextView textView = findViewById(R.id.textView);
        TextView textView2 = findViewById(R.id.textView2);
        TextView textView3 = findViewById(R.id.textView3);
        TextInputEditText textInputEditText = findViewById(R.id.textInputEditText);


        String sol = textView3.getText().toString();
        String sollutions = sol.replace("/10","");
        int int_solluted = Integer.parseInt(sollutions);
        int int_need_solluted = Integer.parseInt(sol.replace(sollutions+"/",""));

        switch (view.getId()) {
            case R.id.goBtn:
                enterBtn.setEnabled(true);
                textView.setBackground(textView2.getBackground());
                textInputEditText.setText("");
                setUp();
                break;

            case R.id.enterBtn:
                boolean rez=Task1.Check(Task1.getNum());
                if (rez){
                    //если задание решено верно
                    int_solluted+=1;
                    sol = int_solluted+"/"+int_need_solluted;
                    if (int_solluted==int_need_solluted){
                        //если решено необходимое количество для текущего уровня, то повышаем уровень пользователя в этом задании
                        int Level = Task1.getLevel();
                        String newLevel = ("Уровень: "+Task1.LevelUp(Level));
                        textView2.setText(newLevel);
                        sol = "0/"+int_need_solluted;
                    }
                    textView3.setText(sol);
                    enterBtn.setEnabled(false);
                }
                break;
        }
    }

    public class Task {
        private int num;


        int getNum() {
            return num;
        }
        void setNum(int x) {
            this.num = x;
        }

        int getNewNum (){
            //АЛГОРИТМ ПОЛУЧЕНИЯ НОМЕРА НОВОГО ЗАДАНИЯ НА ОСНОВАНИИ ПРИОРИТЕТОВ
            int curr_level = Task1.getLevel();
            int min = 1;
            int max = 5;
            switch (curr_level){
                case 1:
                    max = 5;
                    break;
                case 2:
                    max = 20;
                    break;
                case 3:
                    max = 25;
                    break;
            }
            int diff = max - min;

            Random random = new Random();
            int x = random.nextInt(diff+1)+min;
            this.setNum(x);
            return x;
        }


        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        boolean Check(int n){
            TextInputEditText textInputEditText = findViewById(R.id.textInputEditText);
            TextView textView = findViewById(R.id.textView);
            boolean rez;

            if (giveAns(n).equalsIgnoreCase(Objects.requireNonNull(textInputEditText.getText()).toString())){
                textView.setBackgroundResource(R.color.colorAccept);
                rez = true;
            } else {
                textView.setBackgroundResource(R.color.colorDeny);
                rez = false;
            }
            return rez;
        }


        int getLevel (){
            //возвращает текущий уровень
            int Level;
            TextView textView2 = findViewById(R.id.textView2);
            Level = Integer.parseInt(textView2.getText().toString().replace("Уровень: ",""));
            return (Level) ;
        }
        int LevelUp(int Level) {
            int LevelEquals = Level;
            if (LevelEquals <= 3) {
                LevelEquals += 1;
            }
            return (LevelEquals);
        }

    }

    //настроечки размера и стиля шрифта
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            default:
                return true;
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // читаем размер шрифта из EditTextPreference
        Float fSize = Float.parseFloat(Objects.requireNonNull(prefs.getString(getString(R.string.pref_size), "14")));
        // применяем настройки в текстовом поле
        TextView textView = findViewById(R.id.textView);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,fSize);
        TextInputEditText textInputEditText = findViewById(R.id.textInputEditText);
        textInputEditText.setTextSize(TypedValue.COMPLEX_UNIT_SP,fSize);

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
}


/*
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View view) {
        Task Task1 = new Task();
        TextView textView = findViewById(R.id.textView);
        TextView textView2 = findViewById(R.id.textView2);
        TextView textView3 = findViewById(R.id.textView3);
        //определение количества решенных заданий
        String sol = textView3.getText().toString();
        String sollutions = sol.replace("/10","");
        int int_solluted = Integer.parseInt(sollutions);
        int int_need_solluted = Integer.parseInt(sol.replace(sollutions+"/",""));
        //определение номера задания
        String number = Task1.getNumber();
        Button enterBtn = findViewById(R.id.enterBtn);

        switch (view.getId()) {
            case R.id.goBtn:
                //установка нового условия, если нажата клавиша "Далее"
                Task1.setNewUsl(number);
                enterBtn.setEnabled(true);
                textView.setBackground(textView2.getBackground());
                break;

            case R.id.enterBtn:
                //проверка текущего задания, если нажата клавиша "Ввод"
                boolean rez=Task1.Check(number);
                if (rez){
                    //если задание решено верно
                    int_solluted+=1;
                    sol = int_solluted+"/"+int_need_solluted;
                    if (int_solluted==int_need_solluted){
                        //если решено необходимое количество для текущего уровня, то повышаем уровень пользователя в этом задании
                        int Level = Task1.getLevel();
                        String newLevel = ("Уровень: "+Task1.LevelUp(Level));
                        textView2.setText(newLevel);
                        sol = "0/"+int_need_solluted;
                    }
                    textView3.setText(sol);
                    enterBtn.setEnabled(false);
                    }
                break;
        }
    }
    public class Task {
        int getLevel (){
            //возвращает текущий уровень
            int Level;
            TextView textView2 = findViewById(R.id.textView2);
            Level = Integer.parseInt(textView2.getText().toString().replace("Уровень: ",""));
           return (Level) ;
        }
        String getNumber(){
            //возвращает номер текущего задания
            int y = 0;
            TextView textView = findViewById(R.id.textView);
            String Uslovie = textView.getText().toString();
            boolean check = false;
            String number = null;
            String[] names = getResources().getStringArray(R.array.task1_level3);
            while (!check) {
                int resourseID = getResources().getIdentifier(names[y], "string", getPackageName());
                String resourse = getResources().getString(resourseID);
                if (resourse.equalsIgnoreCase(Uslovie)) {
                    check = true;
                    number = names[y];
                }
                y += 1;
            }
            return number;
        }
        void setNewUsl(String number) {
            int currLevel = this.getLevel();
            String[] names = new String[0];
            //в зависимости от текущего уровня подключаем соответствующие ресурсы
            switch (currLevel){
                case 1:
                    names = getResources().getStringArray(R.array.task1_level1);
                    break;
                case 2:
                    names = getResources().getStringArray(R.array.task1_level2);
                    break;
                case 3:
                    names = getResources().getStringArray(R.array.task1_level3);
                    break;
            }
            //определения числового номера задания
            String nomer = number.replace("task", "");
            int nomer_zadaniya = Integer.parseInt(nomer)-1;
            //формирование случайным образом следующего задания
            int min = 0;
            int max = names.length-2;
            int diff = max - min;
            Random random = new Random();
            int x = random.nextInt(diff + 1);
            x += min;
            if (x == nomer_zadaniya) {
                x += 1;
            }
            //получаем необходимую строку с условием
            int resourseID = getResources().getIdentifier(names[x], "string", getPackageName());
            //установка нового условия
            TextView textView = findViewById(R.id.textView);
            textView.setText(getString(resourseID));
            //обнуляем значение TextInputLayout1
            TextInputEditText TextInputLayout1 = findViewById(R.id.TextInputLayout1);
            TextInputLayout1.setText("");
        }
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        boolean Check(String number) {
            TextInputEditText TextInputLayout1 = findViewById(R.id.TextInputLayout1);
            String getAnswer = Objects.requireNonNull(TextInputLayout1.getText()).toString();
            //приводим значение TextInputLayout1 к типу String для дальнейшего сравнения
            String name = number.replace("task", "answer");
            int ID = getResources().getIdentifier(name, "string", getPackageName());
            String ans = getString(ID);
            //сама проверка: при правильно выполненном задании окрашивает текст в зеленый цвет; при неправильно выполненном - в красный
            boolean truth = (getAnswer.equalsIgnoreCase(ans));
            TextView textView = findViewById(R.id.textView);
            if (truth) {
                textView.setBackgroundResource(R.color.colorAccept);
            } else {
                textView.setBackgroundResource(R.color.colorDeny);
            }
            //в зависимости от результата возвращаем логическую переменную
            return(truth);
        }
        int LevelUp(int Level) {
            int LevelEquals = Level;
            if (LevelEquals <= 2) {
                LevelEquals += 1;
            }
            return (LevelEquals);
        }

        //пока не определились с этой процедурой, но на всякий случай пока оставлю
        public void LevelDown() {
            int LevelEquals = getResources().getInteger(R.integer.LevelEquals);
            if (LevelEquals >= 2) {
                LevelEquals -= 1;
            }
        }*/

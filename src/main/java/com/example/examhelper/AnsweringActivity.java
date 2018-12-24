package com.example.examhelper;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;
import java.util.Random;

public class AnsweringActivity extends AppCompatActivity implements View.OnClickListener {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answering);
        Button enterBtn = findViewById(R.id.enterBtn);
        Button goBtn = findViewById(R.id.goBtn);
        goBtn.setOnClickListener(this);
        enterBtn.setOnClickListener(this);
        // Обработчики нажатия кнопок
    }

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
        /*
        //пока не определились с этой процедурой, но на всякий случай пока оставлю
        public void LevelDown() {
            int LevelEquals = getResources().getInteger(R.integer.LevelEquals);
            if (LevelEquals >= 2) {
                LevelEquals -= 1;
            }
        }*/
    }
}

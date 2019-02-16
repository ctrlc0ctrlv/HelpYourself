package com.example.examhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Test_AnsweringActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textView;
    TextView textView3;
    TextView textView4;

    Button enterBtn;
    Button goBtn;

    Bundle arguments;

    AlertDialog.Builder ad;

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
        //инициализируем кнопки
        enterBtn = findViewById(R.id.enterBtn);
        enterBtn.setText("Назад");
        enterBtn.setOnClickListener(this);
        goBtn = findViewById(R.id.goBtn);
        goBtn.setOnClickListener(this);
        //инициализируем переменные
        arguments = getIntent().getExtras();
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
        textView3.setText("");
        textView3.append(String.valueOf(arguments.getInt("number")));
        textView3.append("/");
        textView3.append(String.valueOf(arguments.getInt("num_of_tasks")));
        textView4.append(" " + arguments.getString("id"));
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
}

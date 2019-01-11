package com.example.examhelper;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.examhelper.data.CustomDbHelper;

import java.util.Objects;

public class DataBaseActivity extends AppCompatActivity implements View.OnClickListener {
    private Spinner spinner;
    private int Level = 1;
    CustomDbHelper cDbHelper;
    SQLiteDatabase cDb;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_set);
        spinner = findViewById(R.id.spinner);
        setupSpinner();
        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);
        cDbHelper = new CustomDbHelper(this);
        cDb = cDbHelper.getWritableDatabase();
    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.levels, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(genderSpinnerAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);

                String[] choose = getResources().getStringArray(R.array.levels);
                Toast toast = Toast.makeText(getApplicationContext(),
                        "Ваш выбор: " + choose[position], Toast.LENGTH_SHORT);
                toast.show();

                final String Level1 = getResources().getString(R.string.Level1);
                final String Level2 = getResources().getString(R.string.Level2);
                final String Level3 = getResources().getString(R.string.Level3);
                if (!(TextUtils.isEmpty(selection))) {
                        if (selection.equals(Level1)) {
                            Level = 1;
                        }
                        if (selection.equals(Level2)) {
                            Level = 2;
                        }
                        if (selection.equals(Level3)) {
                            Level = 3;
                        }
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
                Level = 0;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void insertTasks(){
        // Считываем данные из текстовых полей
        TextInputEditText TextInputEditText = findViewById(R.id.TextInputEditText);
        String Uslovie = Objects.requireNonNull(TextInputEditText.getText()).toString();

        TextInputEditText TextInputEditText2 = findViewById(R.id.TextInputEditText2);
        String Answer = Objects.requireNonNull(TextInputEditText2.getText()).toString();

        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        int Number  = arguments.getInt("number");

        ContentValues values = new ContentValues();
        values.put("uslovie", Uslovie);
        values.put("answer", Answer);
        values.put("level", Level);
        values.put("number", Number);

        Log.d("myLogs",Uslovie);
        Log.d("myLogs",Answer);
        Log.d("myLogs", String.valueOf(Level));
        Log.d("myLogs", String.valueOf(Number));

        String TABLE_SUBJECT_NAME = arguments.getString("subject");
        Log.d("myLogs", TABLE_SUBJECT_NAME);
        // Вставляем новый ряд в базу данных и запоминаем его идентификатор
        long newRowId = cDb.insert(TABLE_SUBJECT_NAME, null, values);

        // Выводим сообщение в успешном случае или при ошибке
        if (newRowId == -1) {
            // Если ID  -1, значит произошла ошибка
            Toast.makeText(this, "Ошибка при добавлении задания", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Задание добавлено под номером: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
            insertTasks();
        }
    }

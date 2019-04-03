package com.example.examhelper;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.examhelper.data.CustomDbHelper;

public class DataBaseActivity extends AppCompatActivity implements View.OnClickListener {
    int Level = 1;
    CustomDbHelper cDbHelper;
    SQLiteDatabase cDb;
    private Spinner spinner;
    TextInputEditText TextInputEditText;
    TextInputEditText TextInputEditText2;
    TextInputEditText TextInputEditText3;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_set);
        spinner = findViewById(R.id.spinner);
        setupSpinner();

        TextInputEditText = findViewById(R.id.TextInputEditText);
        TextInputEditText2 = findViewById(R.id.TextInputEditText2);
        TextInputEditText3 = findViewById(R.id.TextInputEditText3);

        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);

        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);

        cDbHelper = new CustomDbHelper(this);
        cDb = cDbHelper.getWritableDatabase();
    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.levels, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(genderSpinnerAdapter);
        spinner.setSelection(0);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int position, long id) {
                String[] choose = getResources().getStringArray(R.array.levels);
                Toast toast = Toast.makeText(getApplicationContext(), "Ваш выбор: " + choose[position], Toast.LENGTH_SHORT);
                toast.show();

                Level = position + 1;
            }
            public void onNothingSelected(AdapterView<?> parent) {
                Level = 0;
            }
        });
    }

    public void insertTask() {
        // Считываем данные из текстовых полей
        Editable uslovie = TextInputEditText.getText();
        assert uslovie != null;
        String Uslovie = uslovie.toString();

        Editable answer = TextInputEditText2.getText();
        assert answer != null;
        String Answer = answer.toString();

        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        int Number = arguments.getInt("number");

        ContentValues values = new ContentValues();
        values.put("uslovie", Uslovie);
        values.put("answer", Answer);
        values.put("level", Level);
        values.put("number", Number);

        String TABLE_SUBJECT_NAME = arguments.getString("subject");
        Log.d("myLogs", TABLE_SUBJECT_NAME);
        // Вставляем новый ряд в базу данных и запоминаем его идентификатор

        if ((Uslovie.equals("")) | (Answer.equals(""))) {
            Toast.makeText(this, "Убедитесь, что вы заполнили все поля для ввода", Toast.LENGTH_SHORT).show();
        } else {
            // Выводим сообщение в успешном случае или при ошибке
            long newRowId = cDb.insert(TABLE_SUBJECT_NAME, null, values);
            if (newRowId == -1) {
                // Если ID  -1, значит произошла ошибка
                Toast.makeText(this, "Ошибка при добавлении задания", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Задание добавлено под номером: " + newRowId, Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void changeTask() {
        // Считываем данные из текстовых полей
        Editable uslovie = TextInputEditText.getText();
        assert uslovie != null;
        String Uslovie = uslovie.toString();

        Editable answer = TextInputEditText2.getText();
        assert answer != null;
        String Answer = answer.toString();

        Editable id = TextInputEditText3.getText();
        assert id != null;
        String ID = id.toString();

        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        int Number = arguments.getInt("number");

        ContentValues values = new ContentValues();
        values.put("uslovie", Uslovie);
        values.put("answer", Answer);
        values.put("level", Level);
        values.put("number", Number);

        String TABLE_SUBJECT_NAME = arguments.getString("subject");
        Log.d("myLogs", TABLE_SUBJECT_NAME);
        // Выводим сообщение в успешном случае или при ошибке
        if ((ID.equals("")) | (Uslovie.equals("")) | (Answer.equals(""))) {
            Toast.makeText(this, "Убедитесь, что вы заполнили все поля для ввода", Toast.LENGTH_SHORT).show();
        } else {
            long newRowId = cDb.update(TABLE_SUBJECT_NAME, values, "_ID ==?", new String[]{ID});
            if (newRowId == -1) {
                // Если ID  -1, значит произошла ошибка
                Toast.makeText(this, "Ошибка при изменении задания", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Задание под номером " + (ID) + " успешно изменено", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void deleteTask() {
        // Считываем данные из текстовых полей
        Editable id = TextInputEditText3.getText();
        assert id != null;
        String ID = id.toString();

        Bundle arguments = getIntent().getExtras();
        assert arguments != null;

        String TABLE_SUBJECT_NAME = arguments.getString("subject");
        Log.d("myLogs", TABLE_SUBJECT_NAME);
        // Выводим сообщение в успешном случае или при ошибке
        if ((ID.equals(""))) {
            Toast.makeText(this, "Убедитесь, что вы заполнили все поля для ввода", Toast.LENGTH_SHORT).show();
        } else {
            long newRowId = cDb.delete(TABLE_SUBJECT_NAME, "_ID ==?", new String[]{ID});
            if (newRowId == -1) {
                // Если ID  -1, значит произошла ошибка
                Toast.makeText(this, "Ошибка при удалении задания", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Задание под номером " + ID + " успешно удалено", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                insertTask();
                break;
            case R.id.button2:
                changeTask();
                break;
            case R.id.button3:
                deleteTask();
                break;
        }
    }
}

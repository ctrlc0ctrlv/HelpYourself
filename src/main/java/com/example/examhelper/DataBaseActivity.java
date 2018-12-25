package com.example.examhelper;


import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.examhelper.data.CustomTasksDbHelper;
import com.example.examhelper.data.DataContract;

import java.util.Objects;

public class DataBaseActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinner;
    private int Level = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_set);
        spinner = findViewById(R.id.spinner);
        setupSpinner();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);

    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.levels, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(genderSpinnerAdapter);
        spinner.setSelection(1);
        spinner.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                        if (selection.equals(getString(R.string.Level1))) {
                            Level = DataContract.CustomTasks.LEVEL_1;
                        } else if (selection.equals(getString(R.string.Level2))) {
                            Level = DataContract.CustomTasks.LEVEL_2;
                        } else if (selection.equals(getString(R.string.Level3))){
                            Level = DataContract.CustomTasks.LEVEL_3;
                        }
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
                Level = 0;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onClick(View v) {
            // Считываем данные из текстовых полей
            TextInputEditText TextInputEditText = findViewById(R.id.TextInputEditText);
            String Uslovie = Objects.requireNonNull(TextInputEditText.getText()).toString();

            TextInputEditText TextInputEditText2 = findViewById(R.id.TextInputEditText2);
            String Answer = Objects.requireNonNull(TextInputEditText2.getText()).toString();

            Bundle arguments = getIntent().getExtras();
            assert arguments != null;
            Integer Number  = arguments.getInt("number");

        CustomTasksDbHelper mDbHelper = new CustomTasksDbHelper(this);
            SQLiteDatabase db = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DataContract.CustomTasks.COLUMN_USLOVIE, Uslovie);
            values.put(DataContract.CustomTasks.COLUMN_ANSWER, Answer);
            values.put(DataContract.CustomTasks.COLUMN_LEVEL, Level);
            values.put(DataContract.CustomTasks.COLUMN_NUMBER, Number);

            // Вставляем новый ряд в базу данных и запоминаем его идентификатор
            long newRowId = db.insert(DataContract.CustomTasks.TABLE_NAME, null, values);

            // Выводим сообщение в успешном случае или при ошибке
            if (newRowId == -1) {
                // Если ID  -1, значит произошла ошибка
                Toast.makeText(this, "Ошибка при добавлении задания", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Задание добавлено под номером: " + newRowId, Toast.LENGTH_SHORT).show();
            }
        }
    }

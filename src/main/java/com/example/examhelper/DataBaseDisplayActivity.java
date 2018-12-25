package com.example.examhelper;

import android.database.sqlite.SQLiteDatabase;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.examhelper.data.CustomTasksDbHelper;
import com.example.examhelper.data.DataContract;


public class DataBaseDisplayActivity extends AppCompatActivity {

        private CustomTasksDbHelper mDbHelper;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main_data_display);

            FloatingActionButton fab = findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(DataBaseDisplayActivity.this, DataBaseActivity.class);

                    Bundle arguments = getIntent().getExtras();
                    assert arguments != null;
                    Integer Number  = arguments.getInt("number");
                    intent.putExtra("number",Number);

                    startActivity(intent);
                }
            });

            mDbHelper = new CustomTasksDbHelper(this);
        }

        @Override
        protected void onStart() {
            super.onStart();
            displayDatabaseInfo();
        }

        @Override
        public boolean onCreateOptionsMenu(Menu menu) {
            // Inflate the menu; this adds items to the action bar if it is present.
            getMenuInflater().inflate(R.menu.menu_main, menu);
            return true;
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            switch (item.getItemId()) {
                case R.id.action_insert_new_data:
                    // Пока ничего не делаем
                    return true;
                case R.id.action_delete_all_entries:
                    // Пока ничего не делаем
                    return true;
            }

            return super.onOptionsItemSelected(item);
        }

        private void displayDatabaseInfo() {
            // Создадим и откроем для чтения базу данных
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            // Зададим условие для выборки - список столбцов
            String[] projection = {
                    DataContract.CustomTasks._ID,
                    DataContract.CustomTasks.COLUMN_USLOVIE,
                    DataContract.CustomTasks.COLUMN_ANSWER,
                    DataContract.CustomTasks.COLUMN_LEVEL,
                    DataContract.CustomTasks.COLUMN_NUMBER };

            // Делаем запрос
            Cursor cursor = db.query(
                    DataContract.CustomTasks.TABLE_NAME,   // таблица
                    projection,            // столбцы
                    null,                  // столбцы для условия WHERE
                    null,                  // значения для условия WHERE
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null);                   // порядок сортировки

            TextView displayTextView = (TextView) findViewById(R.id.text_view_info);

            try {
                displayTextView.setText("Таблица содержит " + cursor.getCount() + " заданий.\n\n");
                displayTextView.append(DataContract.CustomTasks._ID + " - " +
                        DataContract.CustomTasks.COLUMN_USLOVIE + " - " +
                        DataContract.CustomTasks.COLUMN_ANSWER + " - " +
                        DataContract.CustomTasks.COLUMN_LEVEL + " - " +
                        DataContract.CustomTasks.COLUMN_NUMBER + "\n");

                // Узнаем индекс каждого столбца
                int idColumnIndex = cursor.getColumnIndex(DataContract.CustomTasks._ID);
                int nameColumnIndex = cursor.getColumnIndex(DataContract.CustomTasks.COLUMN_USLOVIE);
                int cityColumnIndex = cursor.getColumnIndex(DataContract.CustomTasks.COLUMN_ANSWER);
                int genderColumnIndex = cursor.getColumnIndex(DataContract.CustomTasks.COLUMN_LEVEL);
                int ageColumnIndex = cursor.getColumnIndex(DataContract.CustomTasks.COLUMN_NUMBER);

                // Проходим через все ряды
                while (cursor.moveToNext()) {
                    // Используем индекс для получения строки или числа
                    int currentID = cursor.getInt(idColumnIndex);
                    String currentName = cursor.getString(nameColumnIndex);
                    String currentCity = cursor.getString(cityColumnIndex);
                    int currentGender = cursor.getInt(genderColumnIndex);
                    int currentAge = cursor.getInt(ageColumnIndex);
                    // Выводим значения каждого столбца
                    displayTextView.append(("\n" + currentID + " - " +
                            currentName + " - " +
                            currentCity + " - " +
                            currentGender + " - " +
                            currentAge));
                }
            } finally {
                // Всегда закрываем курсор после чтения
                cursor.close();
            }
        }
    }

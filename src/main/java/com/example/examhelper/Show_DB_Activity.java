package com.example.examhelper;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class Show_DB_Activity extends AppCompatActivity {

        //Объявим переменные компонентов
        Button button;
        TextView textView;

        //Переменная для работы с БД
        private DatabaseHelper mDBHelper;
        private SQLiteDatabase mDb;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_show_db);

            mDBHelper = new DatabaseHelper(this);

            try {
                mDBHelper.updateDataBase();
            } catch (IOException mIOException) {
                throw new Error("UnableToUpdateDatabase");
            }

            try {
                mDb = mDBHelper.getWritableDatabase();
            } catch (SQLException mSQLException) {
                throw mSQLException;
            }

            //Найдем компоненты в XML разметке
            button = (Button) findViewById(R.id.button);
            textView = (TextView) findViewById(R.id.textView);

            //Пропишем обработчик клика кнопки
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String product = "";

                    Cursor cursor = mDb.rawQuery("SELECT * FROM informatics", null);
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        product += cursor.getString(1) + "\n";
                        cursor.moveToNext();
                    }
                    cursor.close();

                    textView.setText(product);
                }
            });
        }
    }

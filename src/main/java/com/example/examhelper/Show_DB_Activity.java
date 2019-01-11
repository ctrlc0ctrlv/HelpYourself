package com.example.examhelper;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.examhelper.data.CustomDbHelper;

import java.io.IOException;

public class Show_DB_Activity extends AppCompatActivity implements View.OnClickListener{
        //Переменная для работы с БД
        private DefaultTasksDBHelper mDBHelper;
        private SQLiteDatabase mDb;
        private CustomDbHelper cDBHelper;
        private SQLiteDatabase cDb;

        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_db);

        //Найдем компоненты в XML разметке
        Button button6 = findViewById(R.id.button6);
        Button button5 = findViewById(R.id.button5);
        FloatingActionButton fab = findViewById(R.id.fab);
        button6.setOnClickListener(this);
        //button5.setOnClickListener(this);
        //fab.setOnClickListener(this);

        mDBHelper = new DefaultTasksDBHelper(this);
        cDBHelper = new CustomDbHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
            }

        try {
            mDb = mDBHelper.getReadableDatabase();
            //cDb = cDBHelper.getWritableDatabase();
            //cDBHelper.onCreate(cDb);
        } catch (SQLException mSQLException) {
            throw mSQLException;
            }
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button6:
                    TextView text_view_info = findViewById(R.id.text_view_info);
                    String product = "";
                    for (int i=1; i<4; i++){
                        product += getString(R.string.level)+" "+i+":\n";
                        Bundle arguments = getIntent().getExtras();
                        assert arguments != null;
                        String TABLE_SUBJECT_NAME = arguments.getString("subject");
                        int number = arguments.getInt ("number");
                        Cursor cursor = mDb.rawQuery("SELECT * FROM "+TABLE_SUBJECT_NAME+" WHERE level =="+i+" AND number =="+number, null);
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            product += cursor.getInt(0) + ". ";
                            product += cursor.getString(1) + "\n";
                            cursor.moveToNext();
                        }
                        product += "\n";
                        cursor.close();
                    }
                    text_view_info.setText(product);
                    break;
                case R.id.fab:
                    Intent intent = new Intent(Show_DB_Activity.this, DataBaseActivity.class);
                    Bundle arguments = getIntent().getExtras();
                    assert arguments != null;
                    int Number  = arguments.getInt("number");
                    String subject = arguments.getString("subject");
                    intent.putExtra("number",Number);
                    intent.putExtra("subject",subject);
                    startActivity(intent);
                    break;
                case R.id.button5:
                    text_view_info = findViewById(R.id.text_view_info);
                    product = "";
                    for (int i=1; i<4; i++) {
                        product += getString(R.string.level) +" "+i + ":\n";
                        arguments = getIntent().getExtras();
                        assert arguments != null;
                        String TABLE_SUBJECT_NAME = arguments.getString("subject");
                        //подключение к бд с изменяемыми заданиями
                        Cursor cursor = cDb.rawQuery("SELECT * FROM "+TABLE_SUBJECT_NAME+" WHERE level =="+i, null);
                        cursor.moveToFirst();
                        while (!cursor.isAfterLast()) {
                            product += cursor.getInt(0) + ". ";
                            product += cursor.getString(1) + "\n";
                            cursor.moveToNext();
                        }
                        product += "\n";
                        cursor.close();
                    }
                    text_view_info.setText(product);
                    break;
            }
        }
}
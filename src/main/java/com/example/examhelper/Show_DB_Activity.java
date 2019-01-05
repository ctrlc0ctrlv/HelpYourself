package com.example.examhelper;

import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;

public class Show_DB_Activity extends AppCompatActivity implements View.OnClickListener{
        //Переменная для работы с БД
        private DefaultTasksDBHelper mDBHelper;
        private SQLiteDatabase mDb;

        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_db);

        //Найдем компоненты в XML разметке
        Button button6 = findViewById(R.id.button6);
        FloatingActionButton fab = findViewById(R.id.fab);
        button6.setOnClickListener(this);
        fab.setOnClickListener(this);

        mDBHelper = new DefaultTasksDBHelper(this);

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
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.button6:
                    String product = "";
                    Cursor cursor = mDb.rawQuery("SELECT * FROM informatics", null);
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        product += cursor.getString(1) + "\n";
                        cursor.moveToNext();
                    }
                    cursor.close();
                    TextView text_view_info = findViewById(R.id.text_view_info);
                    text_view_info.setText(product);
                    break;
                case R.id.fab:
                    Intent intent = new Intent(Show_DB_Activity.this, DataBaseActivity.class);
                    Bundle arguments = getIntent().getExtras();
                    assert arguments != null;
                    Integer Number  = arguments.getInt("number");
                    intent.putExtra("number",Number);

                    startActivity(intent);
                    break;
            }
        }
}
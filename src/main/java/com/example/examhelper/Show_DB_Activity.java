package com.example.examhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.examhelper.data.CustomDbHelper;

import java.io.IOException;
import java.util.Objects;

public class Show_DB_Activity extends AppCompatActivity implements View.OnClickListener{
        //Переменная для работы с БД
        private DefaultTasksDBHelper mDBHelper;
        private SQLiteDatabase mDb;
        private CustomDbHelper cDBHelper;
        private SQLiteDatabase cDb;
        AlertDialog.Builder ad;


        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_db);

        //Найдем компоненты в XML разметке
        Button button6 = findViewById(R.id.button6);
        Button button5 = findViewById(R.id.button5);
        FloatingActionButton fab = findViewById(R.id.fab);
        button6.setOnClickListener(this);
        button5.setOnClickListener(this);
        fab.setOnClickListener(this);

        mDBHelper = new DefaultTasksDBHelper(this);
        cDBHelper = new CustomDbHelper(this);

        try {
            mDBHelper.updateDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
            }
        try {
            mDb = mDBHelper.getReadableDatabase();
            cDb = cDBHelper.getWritableDatabase();
            cDBHelper.onCreate(cDb);
        } catch (SQLException mSQLException) {
            throw mSQLException;
            }

            //УВЕДОМЛЕНИЕ НА СЛУЧАЙ УДАЛЕНИЯ БАЗЫ С ЗАДАНИЯМИ
            final Context context;
            context = Show_DB_Activity.this;
            String title = "Вы уверены? Это действие невозможно отменить";
            String message = "Будут удалены все задания, кроме предустановленных";
            String yesString = "Да";
            String noString = "Отмена";
            ad = new AlertDialog.Builder(context);
            ad.setTitle(title);  // заголовок
            ad.setMessage(message); // сообщение
            ad.setCancelable(false);
            ad.setPositiveButton(yesString, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) {
                    cDBHelper.onDelete(cDb);
                    TextView text_view_info = findViewById(R.id.text_view_info);
                    text_view_info.setText("");
                    }
                });
            ad.setNegativeButton(noString, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int arg1) { }
            });
        }


    @Override
        public void onClick(View view) {
            Bundle arguments = getIntent().getExtras();
            switch (view.getId()){
                case R.id.fab:
                Intent intent = new Intent(Show_DB_Activity.this, DataBaseActivity.class);
                assert arguments != null;
                int Number  = arguments.getInt("number");
                String subject = arguments.getString("subject");
                intent.putExtra("number",Number);
                intent.putExtra("subject",subject);
                startActivity(intent);
                break;

                case R.id.button6:
                    TextView text_view_info = findViewById(R.id.text_view_info);
                    String product = "";
                    for (int i=1; i<4; i++){
                        product += getString(R.string.level)+" "+i+":\n";
                        arguments = getIntent().getExtras();
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem action_delete_progress = menu.findItem(R.id.action_delete_progress);
        MenuItem action_reload_task = menu.findItem(R.id.action_reload_task);
        action_reload_task.setVisible(false);
        action_delete_progress.setVisible(false);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                Intent intent = new Intent();
                intent.setClass(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_clear_database:
                ad.create();
                ad.show();
            default:
                return true;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onResume() {
        super.onResume();
        Log.d("myLogs", "Resume");
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // читаем размер шрифта из EditTextPreference
        Float fSize = Float.parseFloat(Objects.requireNonNull(prefs.getString(getString(R.string.pref_size), "14")));
        // применяем настройки в текстовом поле
        TextView text_view_info = findViewById(R.id.text_view_info);
        text_view_info.setTextSize(TypedValue.COMPLEX_UNIT_SP, fSize);
        //Применяем настройки стиля шрифта
        String regular = prefs.getString(getString(R.string.pref_style), "");
        int typeface = Typeface.NORMAL;
        assert regular != null;
        if (regular.contains("Полужирный"))
            typeface += Typeface.BOLD;
        if (regular.contains("Курсив"))
            typeface += Typeface.ITALIC;
        // меняем настройки в TextView
        text_view_info.setTypeface(null, typeface);
        text_view_info.setText("");
    }
}
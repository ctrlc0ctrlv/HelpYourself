package com.example.examhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.examhelper.data.CustomDbHelper;

import java.io.IOException;

public class Show_DB_Activity extends AppCompatActivity implements View.OnClickListener {
    AlertDialog.Builder ad;
    AlertDialog.Builder dialog;
    //Переменная для работы с БД
    TryingDBHelper mDBHelper;
    private SQLiteDatabase mDb;
    private CustomDbHelper cDBHelper;
    private SQLiteDatabase cDb;

    String TABLE_SUBJECT_NAME = null;
    int number = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_db);
        //Найдем компоненты в XML разметке
        //Button button6 = findViewById(R.id.button6);
        Button button5 = findViewById(R.id.button5);
        FloatingActionButton fab = findViewById(R.id.fab);
        //button6.setOnClickListener(this);
        button5.setOnClickListener(this);
        fab.setOnClickListener(this);

        createDialog();

        mDBHelper = new TryingDBHelper(this);
        cDBHelper = new CustomDbHelper(this);

        try {
            mDBHelper.copyDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToUpdateDatabase");
        }
        mDb = mDBHelper.getReadableDatabase();
        cDb = cDBHelper.getWritableDatabase();
        cDBHelper.onCreate(cDb);
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
            public void onClick(DialogInterface dialog, int arg1) {
            }
        });

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_map:
                                item.setChecked(true);
                                finish();
                                Intent intent0 = new Intent(getBaseContext(), Show_DB_Activity.class);
                                startActivity(intent0);
                                break;
                            case R.id.action_mail:
                                item.setChecked(true);
                                finish();
                                Intent intent1 = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent1);
                                break;
                            case R.id.action_settings:
                                Intent intent2 = new Intent(getBaseContext(), SettingsActivity.class);
                                startActivity(intent2);
                        }
                        return false;
                    }
                });

        //registerForContextMenu(button6);
    }

    @Override
    public void onClick(View view) {
        Bundle arguments = getIntent().getExtras();
        switch (view.getId()) {
            case R.id.fab:
                Intent intent = new Intent(Show_DB_Activity.this, DataBaseActivity.class);
                assert arguments != null;
                int Number = arguments.getInt("number");
                String subject = arguments.getString("subject");
                intent.putExtra("number", Number);
                intent.putExtra("subject", subject);
                startActivity(intent);
                break;
            /*case R.id.button6:
                for (int i = 1; i < 4; i++) {
                    productBuilder.append(getString(R.string.level)).append(" ").append(i).append(":\n");
                    arguments = getIntent().getExtras();
                    assert arguments != null;
                    String TABLE_SUBJECT_NAME = arguments.getString("subject");
                    int number = arguments.getInt("number");
                    Log.d("myLogs", String.valueOf(number));

                    Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_SUBJECT_NAME + " WHERE level ==" + i + " AND number ==" + number, null);
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        productBuilder.append(cursor.getInt(0)).append(". ");
                        productBuilder.append(cursor.getString(1)).append("\n");
                        cursor.moveToNext();
                    }
                    productBuilder.append("\n");
                    cursor.close();
                }
                String product = productBuilder.toString();
                text_view_info.setText(product);
                break;*/
            case R.id.button5:
                TextView text_view_info = findViewById(R.id.text_view_info);
                StringBuilder productBuilder = new StringBuilder();
                text_view_info = findViewById(R.id.text_view_info);
                String product = "";
                StringBuilder productBuilder1 = new StringBuilder(product);
                for (int i = 1; i < 4; i++) {
                    productBuilder1.append(getString(R.string.level)).append(" ").append(i).append(":\n");
                    arguments = getIntent().getExtras();
                    assert arguments != null;
                    String TABLE_SUBJECT_NAME = arguments.getString("subject");
                    int number = arguments.getInt("number");
                    //подключение к бд с изменяемыми заданиями
                    Cursor cursor = cDb.rawQuery("SELECT * FROM " + TABLE_SUBJECT_NAME + " WHERE level ==" + i + " AND number ==" + number, null);
                    cursor.moveToFirst();
                    while (!cursor.isAfterLast()) {
                        productBuilder1.append(cursor.getInt(0)).append(". ");
                        productBuilder1.append(cursor.getString(1)).append("\n");
                        cursor.moveToNext();
                    }
                    productBuilder1.append("\n");
                    cursor.close();
                }
                product = productBuilder1.toString();
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
        switch (item.getItemId()) {
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

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        // читаем размер шрифта из EditTextPreference
        String f_Size = prefs.getString(getResources().getString(R.string.pref_size), "14");
        assert f_Size != null;
        float fSize = Float.parseFloat(f_Size);
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

    @Override
    public void onBackPressed() {
        dialog.create();
        dialog.show();
    }

    void createDialog() {
        //прописываем уведомление
        final Context context;
        context = Show_DB_Activity.this;
        String title = "Вы уверены?";
        String message = "Выйти из приложения?";
        String yesString = "Да";
        String noString = "Отмена";
        dialog = new AlertDialog.Builder(context);
        dialog.setTitle(title);  // заголовок
        dialog.setMessage(message); // сообщение
        dialog.setCancelable(false);
        dialog.setPositiveButton(yesString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                finish();
            }
        });
        dialog.setNegativeButton(noString, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
    }
}
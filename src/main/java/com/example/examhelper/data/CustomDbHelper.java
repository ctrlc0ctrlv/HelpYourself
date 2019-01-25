package com.example.examhelper.data;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class CustomDbHelper extends SQLiteOpenHelper {

    /*Имя файла базы данных*/
    private static final String DATABASE_NAME = "custom1.db";

    /*Версия базы данных. При изменении схемы увеличить на единицу*/
    private static final int DATABASE_VERSION = 1;

    /**
     * Конструктор {@link CustomDataContract.CustomTasks}.
     *
     * @param context Контекст приложения
     */
    public CustomDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*Вызывается при создании базы данных*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_PROGRESS_TABLE_INFORMATICS = "CREATE TABLE IF NOT EXISTS " + CustomDataContract.CustomTasks.TABLE_INFORMATICS_NAME + " ("
                + CustomDataContract.CustomTasks._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CustomDataContract.CustomTasks.COLUMN_USLOVIE + " TEXT NOT NULL, "
                + CustomDataContract.CustomTasks.COLUMN_ANSWER + " TEXT NOT NULL, "
                + CustomDataContract.CustomTasks.COLUMN_LEVEL + " INTEGER NOT NULL DEFAULT 1, "
                + CustomDataContract.CustomTasks.COLUMN_NUMBER + " INTEGER NOT NULL DEFAULT 1)";

        String SQL_CREATE_PROGRESS_TABLE_RUSSIAN = "CREATE TABLE IF NOT EXISTS " + CustomDataContract.CustomTasks.TABLE_RUSSIAN_NAME + " ("
                + CustomDataContract.CustomTasks._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CustomDataContract.CustomTasks.COLUMN_USLOVIE + " TEXT NOT NULL, "
                + CustomDataContract.CustomTasks.COLUMN_ANSWER + " TEXT NOT NULL, "
                + CustomDataContract.CustomTasks.COLUMN_LEVEL + " INTEGER NOT NULL DEFAULT 1, "
                + CustomDataContract.CustomTasks.COLUMN_NUMBER + " INTEGER NOT NULL DEFAULT 1)";

        String SQL_CREATE_PROGRESS_TABLE_MATHS_BASE = "CREATE TABLE IF NOT EXISTS " + CustomDataContract.CustomTasks.TABLE_MATHS_BASE_NAME + " ("
                + CustomDataContract.CustomTasks._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + CustomDataContract.CustomTasks.COLUMN_USLOVIE + " TEXT NOT NULL, "
                + CustomDataContract.CustomTasks.COLUMN_ANSWER + " TEXT NOT NULL, "
                + CustomDataContract.CustomTasks.COLUMN_LEVEL + " INTEGER NOT NULL DEFAULT 1, "
                + CustomDataContract.CustomTasks.COLUMN_NUMBER + " INTEGER NOT NULL DEFAULT 1)";

        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_PROGRESS_TABLE_INFORMATICS);
        db.execSQL(SQL_CREATE_PROGRESS_TABLE_RUSSIAN);
        db.execSQL(SQL_CREATE_PROGRESS_TABLE_MATHS_BASE);
    }


    /*Вызывается при обновлении базы данных*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Запишем в журнал
        Log.w("SQLite", "Обновляемся с версии " + oldVersion + " на версию " + newVersion);
        // Удаляем старую таблицу и создаём новую
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_NAME);
        // Создаём новую таблицу
        onCreate(db);
    }

    /*Вызывается при удалении базы данных*/
    public void onDelete (SQLiteDatabase db){
        db.delete(CustomDataContract.CustomTasks.TABLE_INFORMATICS_NAME, null, null);
    }
}

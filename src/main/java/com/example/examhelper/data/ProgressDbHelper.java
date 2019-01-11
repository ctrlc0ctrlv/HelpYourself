package com.example.examhelper.data;

import android.content.Context;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class ProgressDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = ProgressDbHelper.class.getSimpleName();

    /*Имя файла базы данных*/
    private static final String DATABASE_NAME = "progress.db";

    /*Версия базы данных. При изменении схемы увеличить на единицу*/
    private static final int DATABASE_VERSION = 12;

    /**
     * Конструктор {@link ProgressDataContract.ProgressInTasks}.
     *
     * @param context Контекст приложения
     */
    public ProgressDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*Вызывается при создании базы данных*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_PROGRESS_TABLE_INFORMATICS = "CREATE TABLE IF NOT EXISTS " + ProgressDataContract.ProgressInTasks.TABLE_INFORMATICS_NAME + " ("
                + ProgressDataContract.ProgressInTasks._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProgressDataContract.ProgressInTasks.COLUMN_LEVEL + " INTEGER NOT NULL DEFAULT 1, "
                + ProgressDataContract.ProgressInTasks.COLUMN_NUMBER + " INTEGER NOT NULL DEFAULT 1, "
                + ProgressDataContract.ProgressInTasks.COLUMN_SOLVED + " INTEGER NOT NULL DEFAULT 0)";

        String SQL_CREATE_PROGRESS_TABLE_RUSSIAN = "CREATE TABLE IF NOT EXISTS " + ProgressDataContract.ProgressInTasks.TABLE_RUSSIAN_NAME + " ("
                + ProgressDataContract.ProgressInTasks._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProgressDataContract.ProgressInTasks.COLUMN_LEVEL + " INTEGER NOT NULL DEFAULT 1, "
                + ProgressDataContract.ProgressInTasks.COLUMN_NUMBER + " INTEGER NOT NULL DEFAULT 1, "
                + ProgressDataContract.ProgressInTasks.COLUMN_SOLVED + " INTEGER NOT NULL DEFAULT 0)";

        String SQL_CREATE_PROGRESS_TABLE_MATHS_BASE = "CREATE TABLE IF NOT EXISTS " + ProgressDataContract.ProgressInTasks.TABLE_MATHS_BASE_NAME + " ("
                + ProgressDataContract.ProgressInTasks._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ProgressDataContract.ProgressInTasks.COLUMN_LEVEL + " INTEGER NOT NULL DEFAULT 1, "
                + ProgressDataContract.ProgressInTasks.COLUMN_NUMBER + " INTEGER NOT NULL DEFAULT 1, "
                + ProgressDataContract.ProgressInTasks.COLUMN_SOLVED + " INTEGER NOT NULL DEFAULT 0)";

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
        db.delete(ProgressDataContract.ProgressInTasks.TABLE_INFORMATICS_NAME, null, null);
    }
}

package com.example.examhelper.data;

import android.database.sqlite.SQLiteOpenHelper;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class CustomTasksDbHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = CustomTasksDbHelper.class.getSimpleName();

    /*Имя файла базы данных*/
    private static final String DATABASE_NAME = "custom.db";

    /*Версия базы данных. При изменении схемы увеличить на единицу*/
    private static final int DATABASE_VERSION = 1;

    /**
     * Конструктор {@link com.example.examhelper.data.DataContract.CustomTasks}.
     *
     * @param context Контекст приложения
     */
    public CustomTasksDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }


    /*Вызывается при создании базы данных*/
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Строка для создания таблицы
        String SQL_CREATE_GUESTS_TABLE = "CREATE TABLE " + GuestEntry.TABLE_NAME + " ("
                + DataContract.CustomTasks._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DataContract.CustomTasks.COLUMN_NAME + " TEXT NOT NULL, "
                + DataContract.CustomTasks.COLUMN_CITY + " TEXT NOT NULL, "
                + GuestEntry.COLUMN_GENDER + " INTEGER NOT NULL DEFAULT 3, "
                + GuestEntry.COLUMN_AGE + " INTEGER NOT NULL DEFAULT 0);";

        // Запускаем создание таблицы
        db.execSQL(SQL_CREATE_GUESTS_TABLE);
    }


    /*Вызывается при обновлении базы данных*/
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}

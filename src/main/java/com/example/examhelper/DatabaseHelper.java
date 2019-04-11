package com.example.examhelper;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DB_VERSION = 13;
    //The Android's default system path of your application database.
    private static String DB_PATH = "/data/data/com.example.examhelper/databases/";
    private static String DB_NAME = "default (3).db";

    private final Context myContext;

    private SQLiteDatabase myDataBase;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.myContext = context;
        createDatabaseAlt();
        myDataBase = this.getWritableDatabase();
    }

    /**
     * NOTE NOT USED SEE createDatabaseAlt below (it's replacment)
     * Создает пустую базу данных в системе и переписывает ее с помощью собственной базы данных.
     */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (!dbExist) {
            this.getWritableDatabase(); //<<<<<<<<<< myDatabase will be null
            myDataBase = this.getWritableDatabase();//Создает и/или открывает базу данных
        }
        if (myDataBase.isOpen()) {
            myDataBase.close();
        }
        try {
            copyDataBase(); //<<<<<<<<<< Copy fails due to app_database.db (Read-only file system)Error copying database
        } catch (IOException e) {
            throw new Error(e + "Error copying database");
        }
    }

    // Alternative Create Database
    private void createDatabaseAlt() {
        if (!checkDB()) {
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error(e + "Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        String path = DB_PATH + DB_NAME;
        try {
            checkDB = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //database does't exist yet
        }
        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;
    }

    // Alternative Check that doesn't open the database to create the directory
    private boolean checkDB() {
        File db = new File(myContext.getDatabasePath(DB_NAME).getPath());
        if (db.exists()) return true;
        File dbdir = new File(db.getParent());
        if (!dbdir.exists()) {
            dbdir.mkdirs();
        }
        return false;
    }


    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     */
    private void copyDataBase() throws IOException { //Откройте локальный db в качестве входного потока
        InputStream myInput = myContext.getAssets().open(DB_NAME); //Путь к только что созданному пустому db

        //String outFileName = DB_PATH + DB_NAME; //Откройте пустой бит в качестве выходного потока
        OutputStream myOutput = new FileOutputStream(myContext.getDatabasePath(DB_NAME).getPath());
        //передавать байты из входного файла в выходной файл
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        //Закрыть потоки
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    /**
     * Not used NOTE this open database as read-only and thus was the cause of one of the issues
     */
    public void openDataBase() throws SQLException {
        String myPath = DB_PATH;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }


    //  ПОЛУЧИТЬ
    Cursor getAllData(String table_name) {
        myDataBase = getReadableDatabase();
        return myDataBase.query(table_name, null, null, null, null, null, null);
    }


    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
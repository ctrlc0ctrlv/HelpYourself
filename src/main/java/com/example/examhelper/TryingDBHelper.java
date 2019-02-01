package com.example.examhelper;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class TryingDBHelper extends SQLiteOpenHelper {
        private static String DB_PATH = "/data/data/com.example.examhelper/databases/";
        private static String DB_NAME = "default (1).db";
        private SQLiteDatabase dataBase;
        private final Context fContext;

        TryingDBHelper(Context context) {
            super(context, DB_NAME, null, 24);
            this.fContext = context;
        }
        public void createDataBase() throws IOException {
            boolean dbExist = checkDataBase();
            if (!dbExist) {
                this.getReadableDatabase();
                try {
                    copyDataBase();
                } catch (IOException e) {
                    throw new Error(e);
                }
            }
        }
        private boolean checkDataBase() {
            SQLiteDatabase checkDB = null;
            try {
                Log.d("myLogs", "Checking database...");
                String myPath = DB_PATH + DB_NAME;
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
            } catch (SQLiteException e) {
                Log.d("myLogs", "There is no such database");
                //файл базы данных отсутствует
            }
            if (checkDB != null) {
                checkDB.close();
            }
            return checkDB != null;
        }
        public void copyDataBase() throws IOException {
            Log.d("myLogs", "Coping database...");
            InputStream input = fContext.getAssets().open(DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            OutputStream output = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = input.read(buffer)) > 0) {
                output.write(buffer, 0, length);
            }
            output.flush();
            output.close();
            input.close();
        }
        public void openDataBase() throws SQLException {
            Log.d("myLogs", "Opening database...");
            String path = DB_PATH + DB_NAME;
            dataBase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READONLY);
        }
        @Override
        public synchronized void close() {
            if (dataBase != null)
                dataBase.close();
            super.close();
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("myLogs","onCreate TryingDBHelper");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        }
    }

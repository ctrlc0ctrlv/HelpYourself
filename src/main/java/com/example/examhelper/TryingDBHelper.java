package com.example.examhelper;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipInputStream;

public class TryingDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "default (3).db";
    //@SuppressLint("SdCardPath")
    private String DB_PATH = "/data/data/com.example.examhelper/databases/";
        private SQLiteDatabase dataBase;
        private final Context fContext;

        TryingDBHelper(Context context) {
            super(context, DB_NAME, null, 19);
            this.fContext = context;
            Log.d("myLogs", DB_PATH);
            try {
                File dbFile = new File(DB_PATH + DB_NAME);
                Log.d("myLogs", "db deleted: " + dbFile.delete());
                //copyDataBase();
                copyFromZipFile();
            } catch (IOException e) {
                Log.d("myLogs", "ERROR IN CONSTRUCTOR");
            }
        }

        /*void createDataBase() throws IOException {
            boolean dbExist = checkDataBase();
            if (!dbExist) {
                this.getReadableDatabase();
                try {
                    copyDataBase();
                } catch (IOException e) {
                    Log.d("myLogs","error while coping");
                }
            }
        }*/

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

        void copyDataBase() throws IOException {
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

    private void copyFromZipFile() throws IOException {
        InputStream is = fContext.getResources().openRawResource(R.raw.def);
        OutputStream myOutput = new FileOutputStream(DB_PATH + DB_NAME);
        ZipInputStream zis = new ZipInputStream(new BufferedInputStream(is));
        try {
            while (zis.getNextEntry() != null) {
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int count;

                while ((count = zis.read(buffer)) != -1) {
                    baos.write(buffer, 0, count);
                }
                baos.writeTo(myOutput);
            }
        } finally {
            zis.close();
            myOutput.flush();
            myOutput.close();
            is.close();
        }
    }

    private void openDataBase() throws SQLException {
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onConfigure(SQLiteDatabase db) {
        Log.d("myLogs", "configuration changed");
        super.onConfigure(db);
        db.disableWriteAheadLogging();
    }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d("myLogs","onCreate TryingDBHelper");
            openDataBase();
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            if (newVersion > oldVersion) {
                File dbFile = new File(DB_PATH + DB_NAME);
                if (checkDataBase()) {
                    Log.d("myLogs", String.valueOf(dbFile.delete()));
                    try {
                        //copyDataBase();
                        copyFromZipFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Log.d("myLogs", "---UPGRADE FROM " + oldVersion + " TO " + newVersion + "---");
        }
}

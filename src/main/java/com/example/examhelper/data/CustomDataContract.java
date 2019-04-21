package com.example.examhelper.data;

import android.provider.BaseColumns;

class CustomDataContract {
    private CustomDataContract(){

    }

    static final class CustomTasks implements BaseColumns{
        final static String TABLE_INFORMATICS_NAME = "informatics";
        final static String TABLE_RUSSIAN_NAME = "russian";
        final static String TABLE_MATHS_BASE_NAME = "maths_base";

        final static String _ID = BaseColumns._ID;
        final static String COLUMN_USLOVIE = "uslovie";
        final static String COLUMN_ANSWER = "answer";
        final static String COLUMN_LEVEL = "level";
        final static String COLUMN_NUMBER = "number";
        final static String COLUMN_TABLE = "'table'";
    }
}

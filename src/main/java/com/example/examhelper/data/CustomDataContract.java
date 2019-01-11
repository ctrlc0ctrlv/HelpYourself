package com.example.examhelper.data;

import android.provider.BaseColumns;

public class CustomDataContract {
    private CustomDataContract(){

    }

    public static final class CustomTasks implements BaseColumns{
        public final static String TABLE_INFORMATICS_NAME = "informatics";
        public final static String TABLE_RUSSIAN_NAME = "russian";
        public final static String TABLE_MATHS_BASE_NAME = "maths_base";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_USLOVIE = "uslovie";
        public final static String COLUMN_ANSWER = "answer";
        public final static String COLUMN_LEVEL = "level";
        public final static String COLUMN_NUMBER = "number";
    }
}

package com.example.examhelper.data;

import android.provider.BaseColumns;

public class DataContract {
    private DataContract(){

    }

    public static final class CustomTasks implements BaseColumns {
        public final static String TABLE_NAME = "custom_tasks";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_LEVEL = "level";
        public final static String COLUMN_NUMBER = "number";
        public final static String COLUMN_USLOVIE = "uslovie";
        public final static String COLUMN_ANSWER = "answer";

        public static final int LEVEL_1 = 1;
        public static final int LEVEL_2 = 2;
        public static final int LEVEL_3 = 3;
    }

}

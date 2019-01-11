package com.example.examhelper.data;

import android.provider.BaseColumns;

public class ProgressDataContract {
    private ProgressDataContract(){

    }

    public static final class ProgressInTasks implements BaseColumns {
        public final static String TABLE_INFORMATICS_NAME = "informatics";
        public final static String TABLE_RUSSIAN_NAME = "russian";
        public final static String TABLE_MATHS_BASE_NAME = "maths_base";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_LEVEL = "curr_lvl";
        public final static String COLUMN_SOLVED = "curr_solved";
        public final static String COLUMN_NUMBER = "curr_num";
    }
}

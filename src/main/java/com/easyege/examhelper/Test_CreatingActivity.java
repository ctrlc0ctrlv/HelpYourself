package com.easyege.examhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteStatement;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.easyege.examhelper.data.CustomDbHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Test_CreatingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final List<HashMap<String, Object>> items = new ArrayList<>();
    private static final String[] keys = {"line1", "line2", "icon1"};
    private static final int[] controlIds = {R.id.textview_title, R.id.textview_description, R.id.imageview_icon};

    private static final String TEST_PROGRESS = "my_test";
    private static final String TEST_PROGRESS_ANSWER = "my_test_answer";

    private AlertDialog.Builder dialog;
    private Button button;

    private int NUM_OF_TASKS;
    private String SUBJECT_TABLE_NAME;
    private int[] base_ids;
    private TryingDBHelper tryDBHelper;
    private SQLiteDatabase tryDB;

    private Bundle arguments;
    private boolean[] poses;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("myLogs", "created successfuly");
        setContentView(R.layout.activity_test_creating);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);

        tryDBHelper = new TryingDBHelper(this);

        arguments = getIntent().getExtras();
        assert arguments != null;

        NUM_OF_TASKS = arguments.getInt("num_of_tasks");
        SUBJECT_TABLE_NAME = arguments.getString("subject");
        boolean checked = arguments.getBoolean("checked");
        poses = new boolean[NUM_OF_TASKS];

        if (checked) {
            button.setEnabled(false);
            poses = arguments.getBooleanArray("solved");
        }
        //initialise_items(checked);

        final Context context;
        context = Test_CreatingActivity.this;
        String title = "Вы уверены, что хотите выйти?";
        String message = "Ваши ответы в текущем тесте будут аннулированы";
        String yesString = "Выйти";
        String noString = "Отмена";
        dialog = new android.app.AlertDialog.Builder(context);
        dialog.setTitle(title);  // заголовок
        dialog.setMessage(message); // сообщение
        dialog.setCancelable(false);
        dialog.setPositiveButton(yesString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                SharedPreferences activityPreferences = getSharedPreferences(TEST_PROGRESS, Context.MODE_PRIVATE);
                SharedPreferences.Editor ed = activityPreferences.edit();
                ed.clear();
                ed.apply();
                finish();
            }
        });
        dialog.setNegativeButton(noString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });

        ListView listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                start_(position);
            }
        });

        ListAdapter adapter1 = new SimpleAdapter(this, items, R.layout.layer, keys, controlIds);
        listView.setAdapter(adapter1);
    }

    private void start_(int position) {
        Intent intent = new Intent(this, Test_AnsweringActivity.class);
        //intent.setClass(Test_CreatingActivity.this, Test_AnsweringActivity.class);
        intent.putExtra("number", position + 1);
        //intent.putExtra("id", base_ids[position+1]);
        intent.putExtra("base_ids", base_ids);
        intent.putExtra("num_of_tasks", NUM_OF_TASKS);
        intent.putExtra("subject", SUBJECT_TABLE_NAME);
        startActivityForResult(intent, 666);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 666) {
            if (resultCode == 666) {
                checkTest();
            }
        }
        Log.d("myLogs", "requestcode = " + requestCode);
        Log.d("myLogs", "resultcode = " + resultCode);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button) {
            checkTest();
        }
    }

    private void checkTest() {
        button.setEnabled(false);

        String[] curr_ans = getAnswers();
        String[] base_ans = getAnswersFromBase();

        int rez = 0;


        for (int num = 1; num <= NUM_OF_TASKS; num++) {
            if (curr_ans[num].equalsIgnoreCase(base_ans[num])) {
                poses[num - 1] = true;
                rez += 1;

                //listView.getAdapter().getView()
                //viewGroup.getRootView().setBackgroundColor(getResources().getColor(R.color.colorAccept));
            } else {
                poses[num - 1] = false;
            }
        }

        Log.d("myLogs", Arrays.toString(poses));
        Toast.makeText(this, "Решено верно: " + rez + "\nРешено неверно: " + (NUM_OF_TASKS - rez), Toast.LENGTH_LONG).show();
        re_create();
        //Log.d("myLogs", "curr_answers = "+Arrays.toString(curr_answers));
        //Log.d("myLogs", "curr_base_answers = "+Arrays.toString(curr_base_answers));
    }

    private void re_create() {
        Intent intent = getIntent();
        intent.putExtra("checked", true);
        intent.putExtra("base_ids", base_ids);
        intent.putExtra("solved", poses);

        finish();
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        dialog.create();
        dialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putIntArray("base_ids", base_ids);
        outState.putStringArray("answers", getAnswers());
        super.onSaveInstanceState(outState);
        Log.d("myLogs", "onSaveInstanceState");
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        base_ids = savedInstanceState.getIntArray("base_ids");
        Log.d("myLogs", "onRestoreInstanceState");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences activityPreferences = getSharedPreferences(TEST_PROGRESS, Context.MODE_PRIVATE);
        SharedPreferences.Editor ed = activityPreferences.edit();
        ed.clear();
        ed.apply();
        Log.d("myLogs", "cleared successfully");
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean checked = arguments.getBoolean("checked");
        poses = new boolean[NUM_OF_TASKS];

        if (checked) {
            button.setEnabled(false);
            poses = arguments.getBooleanArray("solved");
        }

        insertRecords();

        initialise_items(checked);
        Log.d("myLogs", "onResume");
    }

    private void insertRecords() {
        tryDB = tryDBHelper.getWritableDatabase();
        CustomDbHelper cDBHelper = new CustomDbHelper(this);
        SQLiteDatabase cDb = cDBHelper.getReadableDatabase();
        cDBHelper.onCreate(cDb);

        Cursor ccursor = cDb.rawQuery("SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE level ==1", null);
        Cursor cursor = null;
        boolean err = false;
        try {
            cursor = tryDB.rawQuery("SELECT * FROM " + SUBJECT_TABLE_NAME, null);
        } catch (SQLiteException e) {
            Log.d("myLogs", e.toString());
            err = true;
        }

        if (err) {
            Toast.makeText(this, "База данных копируется, попробуйте еще раз", Toast.LENGTH_LONG).show();
            finish();
        }

        String sql = "INSERT INTO " + SUBJECT_TABLE_NAME + " VALUES(?,?,?,?,?,?)";

        SQLiteStatement statement = null;
        int i = 0;
        int count = 0;


        try {
            statement = tryDB.compileStatement(sql);
            assert cursor != null;
            cursor.moveToLast();
            i = cursor.getInt(0) + 1;
            cursor.close();
        } catch (SQLiteException e) {
            Log.d("myLogs", e.toString());
        }

        assert statement != null;

        tryDB.beginTransaction();
        try {
            ccursor.moveToFirst();
            while (!ccursor.isAfterLast()) {
                statement.clearBindings();
                statement.bindLong(1, i);
                statement.bindString(2, ccursor.getString(1));
                statement.bindString(3, ccursor.getString(2));
                statement.bindLong(4, ccursor.getInt(3));
                statement.bindLong(5, ccursor.getInt(4));
                if (!SUBJECT_TABLE_NAME.equalsIgnoreCase("maths_base")) {
                    statement.bindString(6, "");
                }
                statement.execute();
                Log.d("myLogs", "row " + i);
                i += 1;
                count += 1;
                ccursor.moveToNext();
            }
            tryDB.setTransactionSuccessful();
            Log.d("myLogs", "transaction success: " + count + " rows copied");
        } finally {
            tryDB.endTransaction();
        }
        ccursor.close();
        cDb.close();
        tryDB.close();
    }

    private String[] getAnswers() {
        String[] curr_answers = new String[NUM_OF_TASKS + 1];
        for (int i = 1; i <= NUM_OF_TASKS; i++) {
            SharedPreferences activityPreferences = getSharedPreferences(TEST_PROGRESS, Context.MODE_PRIVATE);
            curr_answers[i] = activityPreferences.getString(TEST_PROGRESS_ANSWER + "_" + i, "");
        }
        return curr_answers;
    }

    private String[] getAnswersFromBase() {
        String[] curr_base_answers = new String[NUM_OF_TASKS + 1];

        tryDB = tryDBHelper.getReadableDatabase();

        for (int x = 1; x <= NUM_OF_TASKS; x++) {
            String raw = "SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE _id == " + base_ids[x];
            Cursor cursor = tryDB.rawQuery(raw, null);

            cursor.moveToFirst();
            curr_base_answers[x] = cursor.getString(2);
            cursor.close();
        }
        tryDB.close();
        return curr_base_answers;
    }

    private void initialise_items(boolean checked) {
        items.clear();
        HashMap<String, Object> map;

        if (!checked) {
            return_base_ids();
        } else {
            base_ids = arguments.getIntArray("base_ids");
        }

        String[] names = new String[0];

        switch (SUBJECT_TABLE_NAME) {
            case "russian":
                names = getResources().getStringArray(R.array.russian);
                break;
            case "maths_base":
                names = getResources().getStringArray(R.array.maths_base);
                break;
            case "informatics":
                names = getResources().getStringArray(R.array.informatics);
                break;
        }

        for (int i = 1; i <= NUM_OF_TASKS; i++) {
            map = new HashMap<>();
            map.put("line1", names[i - 1]);
            map.put("line2", Integer.toString(base_ids[i]));
            if (checked && poses[i - 1]) {
                map.put("icon1", R.drawable.tick);
            }
            items.add(map);
        }
    }

    private void return_base_ids() {
        base_ids = new int[NUM_OF_TASKS + 1];

        tryDB = tryDBHelper.getReadableDatabase();

        Random random;

        for (int x = 1; x < base_ids.length; x++) {
            String raw = "SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE number == " + x;

            Cursor cursor = null;
            boolean err = false;
            try {
                cursor = tryDB.rawQuery(raw, null);
            } catch (SQLiteException e) {
                Log.d("myLogs", e.toString());
                err = true;
            }
            if (err) {
                Toast.makeText(this, "База данных копируется, попробуйте еще раз", Toast.LENGTH_LONG).show();
                finish();
            }

            random = new Random();
            assert cursor != null;
            try {
                int new_id = random.nextInt(cursor.getCount());

                cursor.moveToPosition(new_id);
                base_ids[x] = cursor.getInt(0);
                cursor.close();
            } catch (NullPointerException e) {
                Log.d("myLogs", e.toString());
            }
        }
        Log.d("myLogs", Arrays.toString(base_ids));
        Log.d("myLogs", SUBJECT_TABLE_NAME);
        tryDB.close();
    }
}

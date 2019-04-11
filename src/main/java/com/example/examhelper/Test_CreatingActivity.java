package com.example.examhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Test_CreatingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final List<HashMap<String, Object>> items = new ArrayList<>();
    private static final String[] keys = {"line1", "line2", "icon1"};
    private static final int[] controlIds = {R.id.textview_title, R.id.textview_description, R.id.imageview_icon};

    public static final String TEST_PROGRESS = "my_test";
    public static final String TEST_PROGRESS_ANSWER = "my_test_answer";

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
    public void onCreate(Bundle savedInstanceState) {
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

    void start_(int position) {
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
        Log.d("myLogs", "requestcode = " + String.valueOf(requestCode));
        Log.d("myLogs", "resultcode = " + String.valueOf(resultCode));
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                checkTest();
                break;
        }
    }

    public void checkTest() {
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
        Toast.makeText(this, "Решено верно: " + Integer.toString(rez) + "\nРешено неверно: " + Integer.toString(NUM_OF_TASKS - rez), Toast.LENGTH_LONG).show();
        re_create();
        //Log.d("myLogs", "curr_answers = "+Arrays.toString(curr_answers));
        //Log.d("myLogs", "curr_base_answers = "+Arrays.toString(curr_base_answers));
    }

    void re_create() {
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
    public void onResume() {
        super.onResume();
        boolean checked = arguments.getBoolean("checked");
        poses = new boolean[NUM_OF_TASKS];

        if (checked) {
            button.setEnabled(false);
            poses = arguments.getBooleanArray("solved");
        }
        initialise_items(checked);
        Log.d("myLogs", "onResume");
    }

    String[] getAnswers() {
        String[] curr_answers = new String[NUM_OF_TASKS + 1];
        for (int i = 1; i <= NUM_OF_TASKS; i++) {
            SharedPreferences activityPreferences = getSharedPreferences(TEST_PROGRESS, Context.MODE_PRIVATE);
            curr_answers[i] = activityPreferences.getString(TEST_PROGRESS_ANSWER + "_" + Integer.toString(i), "");
        }
        return curr_answers;
    }

    String[] getAnswersFromBase() {
        String[] curr_base_answers = new String[NUM_OF_TASKS + 1];

        tryDB = tryDBHelper.getReadableDatabase();

        for (int x = 1; x <= NUM_OF_TASKS; x++) {
            String raw = "SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE _id == " + base_ids[x];
            Cursor cursor = tryDB.rawQuery(raw, null);

            cursor.moveToFirst();
            curr_base_answers[x] = cursor.getString(2);
            cursor.close();
        }
        return curr_base_answers;
    }

    void initialise_items(boolean checked) {
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

    void return_base_ids() {
        base_ids = new int[NUM_OF_TASKS + 1];

        tryDB = tryDBHelper.getReadableDatabase();

        Random random;

        tryDBHelper.onCreate(tryDB);
        try {
            tryDBHelper.copyDataBase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        for (int x = 1; x < base_ids.length; x++) {
            String raw = "SELECT * FROM " + SUBJECT_TABLE_NAME + " WHERE number == " + x;
            Cursor cursor = tryDB.rawQuery(raw, null);

            random = new Random();
            int new_id = random.nextInt(cursor.getCount());

            cursor.moveToPosition(new_id);
            base_ids[x] = cursor.getInt(0);
            cursor.close();
        }
        Log.d("myLogs", Arrays.toString(base_ids));
        Log.d("myLogs", SUBJECT_TABLE_NAME);
        tryDB.close();
    }
}

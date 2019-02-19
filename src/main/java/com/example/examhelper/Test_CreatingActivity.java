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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Test_CreatingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final List<Map<String, String>> items = new ArrayList<>();
    private static final String[] keys = {"line1", "line2"};
    private static final int[] controlIds = {android.R.id.text1, android.R.id.text2};

    public static final String TEST_PROGRESS = "my_test";
    public static final String TEST_PROGRESS_ANSWER = "my_test_answer";

    private AlertDialog.Builder dialog;
    private Button button;
    private ListView listView;

    private int NUM_OF_TASKS;
    private String SUBJECT_TABLE_NAME;
    private int[] base_ids;
    private TryingDBHelper tryDBHelper;
    private SQLiteDatabase tryDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_creating);
        button = findViewById(R.id.button);
        button.setOnClickListener(this);

        tryDBHelper = new TryingDBHelper(this);

        Bundle arguments = getIntent().getExtras();
        assert arguments != null;

        NUM_OF_TASKS = arguments.getInt("num_of_tasks");
        SUBJECT_TABLE_NAME = arguments.getString("subject");

        initialise_items();

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

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                Intent intent = new Intent(Test_CreatingActivity.this, Test_AnsweringActivity.class);
                intent.setClass(Test_CreatingActivity.this, Test_AnsweringActivity.class);
                intent.putExtra("number", position + 1);
                //intent.putExtra("id", base_ids[position+1]);
                intent.putExtra("base_ids", base_ids);
                intent.putExtra("num_of_tasks", NUM_OF_TASKS);
                intent.putExtra("subject", SUBJECT_TABLE_NAME);
                startActivityForResult(intent, 1);
            }
        });
        ListAdapter adapter1 = null;


        switch (NUM_OF_TASKS) {
            case 23:
                adapter1 = new SimpleAdapter(this, items, android.R.layout.simple_list_item_2, keys, controlIds);
                break;
            case 20:
                adapter1 = new SimpleAdapter(this, items, android.R.layout.simple_list_item_2, keys, controlIds);
                break;
            case 26:
                adapter1 = new SimpleAdapter(this, items, android.R.layout.simple_list_item_2, keys, controlIds);
                break;
        }
        listView.setAdapter(adapter1);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                checkTest();
                button.setEnabled(false);
                break;
        }
    }

    public void checkTest() {
        String[] curr_ans = getAnswers();
        String[] base_ans = getAnswersFromBase();

        int rez = 0;

        for (int num = 1; num <= NUM_OF_TASKS; num++) {
            if (curr_ans[num].equalsIgnoreCase(base_ans[num])) {
                rez += 1;
            }
        }

        Toast.makeText(this, "Решено верно: " + Integer.toString(rez) + "\nРешено неверно: " + Integer.toString(NUM_OF_TASKS - rez), Toast.LENGTH_LONG).show();

        //Log.d("myLogs", "curr_answers = "+Arrays.toString(curr_answers));
        //Log.d("myLogs", "curr_base_answers = "+Arrays.toString(curr_base_answers));
    }

    @Override
    public void onBackPressed() {
        dialog.create();
        dialog.show();
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

    void initialise_items() {
        items.clear();
        Map<String, String> map;

        return_base_ids();

        for (int i = 1; i <= NUM_OF_TASKS; i++) {
            map = new HashMap<>();
            map.put("line1", "Задание " + i);
            map.put("line2", Integer.toString(base_ids[i]));
            items.add(map);
        }
    }

    void return_base_ids() {
        base_ids = new int[NUM_OF_TASKS + 1];

        tryDB = tryDBHelper.getReadableDatabase();

        Random random;

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
    }
}

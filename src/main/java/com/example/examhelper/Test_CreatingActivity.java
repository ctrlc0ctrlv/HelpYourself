package com.example.examhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

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

    AlertDialog.Builder dialog;
    ListView listView;

    Bundle arguments;
    int NUM_OF_TASKS;
    String SUBJECT_TABLE_NAME;
    int[] base_ids;
    private TryingDBHelper tryDBHelper;
    private SQLiteDatabase tryDB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_creating);

        tryDBHelper = new TryingDBHelper(this);

        arguments = getIntent().getExtras();
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

    }

    @Override
    public void onBackPressed() {
        dialog.create();
        dialog.show();
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

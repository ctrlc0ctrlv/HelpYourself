package com.example.examhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Test_CreatingActivity extends AppCompatActivity implements View.OnClickListener {
    private static final List<Map<String, String>> items = new ArrayList<>();
    private static final String[] keys = {"line1", "line2"};
    private static final int[] controlIds = {android.R.id.text1, android.R.id.text2};

    static {
        Map<String, String> map = new HashMap<>();
        map.put("line1", "Задание 1");
        map.put("line2", "256");
        items.add(map);

        map = new HashMap<>();
        map.put("line1", "Задание 2");
        map.put("line2", "134");
        items.add(map);

        map = new HashMap<>();
        map.put("line1", "Задание 3");
        map.put("line2", "865");
        items.add(map);

        map = new HashMap<>();
        map.put("line1", "Задание 4");
        map.put("line2", "408");
        items.add(map);

        map = new HashMap<>();
        map.put("line1", "Задание 5");
        map.put("line2", "923");
        items.add(map);
    }

    AlertDialog.Builder dialog;
    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_creating);

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
                intent.putExtra("id", items.get(position).get("line2"));

                Bundle arguments = getIntent().getExtras();
                assert arguments != null;

                intent.putExtra("num_of_tasks", arguments.getInt("num_of_tasks"));
                intent.putExtra("subject", arguments.getString("subject"));
                startActivityForResult(intent, 1);
            }
        });
        ListAdapter adapter1 = null;

        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        switch (arguments.getInt("num_of_tasks")) {
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
}

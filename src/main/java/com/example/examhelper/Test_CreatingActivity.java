package com.example.examhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
        map.put("line1", "Мурзик");
        map.put("line2", "Агент 003");
        items.add(map);

        map = new HashMap<>();
        map.put("line1", "Барсик");
        map.put("line2", "Агент 004");
        items.add(map);

        map = new HashMap<>();
        map.put("line1", "Васька");
        map.put("line2", "Агент 005");
        items.add(map);

        map = new HashMap<>();
        map.put("line1", "Рыжик");
        map.put("line2", "Агент 006");
        items.add(map);

        map = new HashMap<>();
        map.put("line1", "Кузя");
        map.put("line2", "Агент 007");
        items.add(map);
    }

    ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_creating);

        listView = findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position, long id) {
                //int activePosition = 0; // первый элемент списка
                //boolean click = listView.performItemClick(listView.getAdapter().getView(activePosition, null, null), activePosition, listView.getAdapter().getItemId(activePosition));

                Intent intent = new Intent(Test_CreatingActivity.this, Test_AnsweringActivity.class);
                intent.setClass(Test_CreatingActivity.this, Test_AnsweringActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        ArrayAdapter<String> adapter = null;
        ListAdapter adapter1 = null;

        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        switch (arguments.getInt("num_of_tasks")) {
            case 23:
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.tasks_informatics));
                adapter1 = new SimpleAdapter(this, items, android.R.layout.simple_list_item_2, keys, controlIds);
                break;
            case 20:
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.tasks_maths_base));
                break;
            case 26:
                adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.tasks_russian));
                break;
        }
        listView.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {

    }
}

package com.example.examhelper;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class New_Test_CreatingActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String TEST_PROGRESS = "my_test";
    public static final String TEST_PROGRESS_ANSWER = "my_test_answer";
    private static final List<HashMap<String, Object>> items = new ArrayList<>();
    private static final String[] keys = {"line1", "line2", "icon1"};
    private static final int[] controlIds = {R.id.textview_title, R.id.textview_description, R.id.imageview_icon};
    Bundle arguments;
    private Button button;
    private AlertDialog.Builder dialog;
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answering);

        button = findViewById(R.id.button);
        button.setOnClickListener(this);

        listView = findViewById(R.id.listView);

        arguments = getIntent().getExtras();
        assert arguments != null;

        initializeDialog();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button:
                checkTest();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();


        initializeItems();
        setUp();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    public void onBackPressed() {
        dialog.create();
        dialog.show();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {


        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState

        );
    }

    public void checkTest() {
        /*
         * Checks the whole test
         */
        button.setEnabled(false);


        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    public void setUp() {
        /*
         * Sets up the listView and other components
         */
        ListAdapter adapter1 = new SimpleAdapter(this, items, R.layout.layer, keys, controlIds);
        listView.setAdapter(adapter1);
    }

    void initializeItems() {
        /*
         * Returns items array - array of task IDs
         */
    }

    void initializeDialog() {
        /*
         * Prepares dialog for future usage
         */
        final Context context;
        context = New_Test_CreatingActivity.this;
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
    }
}

package com.example.examhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChooseSubjectActivity extends AppCompatActivity implements View.OnClickListener {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_sub);
        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        Button button3 = findViewById(R.id.button3);
        // Обработчик нажатия кнопки
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Intent intent;
        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        if (arguments.getBoolean("chosen_test")) {
            intent = new Intent(this, Test_CreatingActivity.class);
        } else {
            intent = new Intent(this, ButtonsActivity.class);

            intent.putExtra("chosen_solve", arguments.getBoolean("chosen_solve"));
            intent.putExtra("chosen_my", arguments.getBoolean("chosen_my"));
        }
        int num_of_tasks=30;

        switch (view.getId()){
            case R.id.button:
                num_of_tasks=23;
                intent.putExtra("subject","informatics");
                break;
            case R.id.button2:
                num_of_tasks=20;
                intent.putExtra("subject", "maths_base");
                break;
            case R.id.button3:
                num_of_tasks=26;
                intent.putExtra("subject", "russian");
                break;
        }
        intent.putExtra("num_of_tasks",num_of_tasks);
        startActivity(intent);
    }
}

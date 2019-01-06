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
        intent = new Intent(this,ButtonsActivity.class);

        int num_of_tasks=30;

        switch (view.getId()){
            case R.id.button:
                num_of_tasks=23;
                break;
            case R.id.button2:
                num_of_tasks=20;
                break;
            case R.id.button3:
                num_of_tasks=26;
                break;
        }
        intent.putExtra("num_of_tasks",num_of_tasks);


        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        boolean chosen_solve = arguments.getBoolean("chosen_solve");
        boolean chosen_my = arguments.getBoolean("chosen_my");
        intent.putExtra("chosen_solve",chosen_solve);
        intent.putExtra("chosen_my", chosen_my);

        startActivity(intent);
    }
}

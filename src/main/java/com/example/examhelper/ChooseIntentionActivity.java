package com.example.examhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class ChooseIntentionActivity extends AppCompatActivity implements View.OnClickListener {

        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_choose_intent);
            Button button = findViewById(R.id.button);
            Button button2 = findViewById(R.id.button2);
            button.setOnClickListener(this);
            button2.setOnClickListener(this);
            // Обработчик нажатия кнопки
        }

        @Override
        public void onClick(View view) {
            Intent intent;
            intent = new Intent(this,ChooseSubjectActivity.class);
            boolean chosen_solve = true;
            boolean chosen_my = false;
            switch (view.getId()){
                case R.id.button:
                    chosen_solve = true;
                    chosen_my = false;
                    break;
                case R.id.button2:
                    chosen_solve = false;
                    chosen_my = true;
                    break;
            }
            intent.putExtra("chosen_solve",chosen_solve);
            intent.putExtra("chosen_my", chosen_my);
            startActivity(intent);
        }
    }
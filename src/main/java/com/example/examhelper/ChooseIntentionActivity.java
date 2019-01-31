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
            Button button3 = findViewById(R.id.button3);
            Button button4 = findViewById(R.id.button4);

            button.setOnClickListener(this);
            button2.setOnClickListener(this);
            button3.setOnClickListener(this);
            button4.setOnClickListener(this);
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
                    intent.putExtra("chosen_solve", true);
                    intent.putExtra("chosen_my", false);
                    startActivity(intent);
                    break;
                case R.id.button2:
                    intent.putExtra("chosen_solve", false);
                    intent.putExtra("chosen_my", true);
                    startActivity(intent);
                    break;
                case R.id.button3:
                    break;
                case R.id.button4:
                    Intent i = new Intent(this,TimeActivity.class);
                    startActivity(i);
                    break;
            }
        }
    }
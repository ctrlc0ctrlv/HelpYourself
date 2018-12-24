package com.example.examhelper;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ButtonsActivity extends AppCompatActivity implements View.OnClickListener {

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buttons);
        //инициализация переменных
        Button buttonObjectChoice1 = findViewById(R.id.buttonChoice1);
        Button buttonObjectChoice2 = findViewById(R.id.buttonChoice2);
        Button buttonObjectChoice3 = findViewById(R.id.buttonChoice3);
        Button buttonObjectChoice4 = findViewById(R.id.buttonChoice4);
        Button buttonObjectChoice5 = findViewById(R.id.buttonChoice5);
        Button buttonObjectChoice6 = findViewById(R.id.buttonChoice6);
        Button buttonObjectChoice7 = findViewById(R.id.buttonChoice7);
        Button buttonObjectChoice8 = findViewById(R.id.buttonChoice8);
        Button buttonObjectChoice9 = findViewById(R.id.buttonChoice9);
        Button buttonObjectChoice10 = findViewById(R.id.buttonChoice10);
        Button buttonObjectChoice11 = findViewById(R.id.buttonChoice11);
        Button buttonObjectChoice12 = findViewById(R.id.buttonChoice12);
        Button buttonObjectChoice13 = findViewById(R.id.buttonChoice13);
        Button buttonObjectChoice14 = findViewById(R.id.buttonChoice14);
        Button buttonObjectChoice15 = findViewById(R.id.buttonChoice15);
        Button buttonObjectChoice16 = findViewById(R.id.buttonChoice16);
        Button buttonObjectChoice17 = findViewById(R.id.buttonChoice17);
        Button buttonObjectChoice18 = findViewById(R.id.buttonChoice18);
        Button buttonObjectChoice19 = findViewById(R.id.buttonChoice19);
        Button buttonObjectChoice20 = findViewById(R.id.buttonChoice20);
        Button buttonObjectChoice21 = findViewById(R.id.buttonChoice21);
        Button buttonObjectChoice22 = findViewById(R.id.buttonChoice22);
        Button buttonObjectChoice23 = findViewById(R.id.buttonChoice23);
        Button buttonObjectChoice24 = findViewById(R.id.buttonChoice24);
        Button buttonObjectChoice25 = findViewById(R.id.buttonChoice25);
        Button buttonObjectChoice26 = findViewById(R.id.buttonChoice26);

        buttonObjectChoice1.setOnClickListener(this);
        buttonObjectChoice2.setOnClickListener(this);
        buttonObjectChoice3.setOnClickListener(this);
        buttonObjectChoice4.setOnClickListener(this);
        buttonObjectChoice5.setOnClickListener(this);
        buttonObjectChoice6.setOnClickListener(this);
        buttonObjectChoice7.setOnClickListener(this);
        buttonObjectChoice8.setOnClickListener(this);
        buttonObjectChoice9.setOnClickListener(this);
        buttonObjectChoice10.setOnClickListener(this);
        buttonObjectChoice11.setOnClickListener(this);
        buttonObjectChoice12.setOnClickListener(this);
        buttonObjectChoice13.setOnClickListener(this);
        buttonObjectChoice14.setOnClickListener(this);
        buttonObjectChoice15.setOnClickListener(this);
        buttonObjectChoice16.setOnClickListener(this);
        buttonObjectChoice17.setOnClickListener(this);
        buttonObjectChoice18.setOnClickListener(this);
        buttonObjectChoice19.setOnClickListener(this);
        buttonObjectChoice20.setOnClickListener(this);
        buttonObjectChoice21.setOnClickListener(this);
        buttonObjectChoice22.setOnClickListener(this);
        buttonObjectChoice23.setOnClickListener(this);
        buttonObjectChoice24.setOnClickListener(this);
        buttonObjectChoice25.setOnClickListener(this);
        buttonObjectChoice26.setOnClickListener(this);

        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        int num = arguments.getInt("num_of_tasks");
        switch (num){
            case 23:
                buttonObjectChoice24.setEnabled(false);
                buttonObjectChoice25.setEnabled(false);
                buttonObjectChoice26.setEnabled(false);
                break;
            case 20:
                buttonObjectChoice21.setEnabled(false);
                buttonObjectChoice22.setEnabled(false);
                buttonObjectChoice23.setEnabled(false);
                buttonObjectChoice24.setEnabled(false);
                buttonObjectChoice25.setEnabled(false);
                buttonObjectChoice26.setEnabled(false);
                break;
            case 26:

                break;
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        Bundle arguments = getIntent().getExtras();
        assert arguments != null;
        boolean chosen_solve = arguments.getBoolean("chosen_solve");

        if (chosen_solve) {
            intent = new Intent(this, AnsweringActivity.class);
        } else {
            intent = new Intent (this,AddTask.class);
        }
        startActivity(intent);
    }
}


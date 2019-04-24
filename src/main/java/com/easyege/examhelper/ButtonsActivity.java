package com.easyege.examhelper;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class ButtonsActivity extends AppCompatActivity implements View.OnClickListener {
    private int NUM;
    private Bundle arguments;

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

        LinearLayout linearLayout = findViewById(R.id.LinearLayout);

        arguments = getIntent().getExtras();
        assert arguments != null;
        NUM = arguments.getInt("num_of_tasks");
        switch (NUM) {
            case 23:
                String[] names = getResources().getStringArray(R.array.informatics);

                buttonObjectChoice1.setText(names[0]);
                buttonObjectChoice2.setText(names[1]);
                buttonObjectChoice3.setText(names[2]);
                buttonObjectChoice4.setText(names[3]);
                buttonObjectChoice5.setText(names[4]);
                buttonObjectChoice6.setText(names[5]);
                buttonObjectChoice7.setText(names[6]);
                buttonObjectChoice8.setText(names[7]);
                buttonObjectChoice9.setText(names[8]);
                buttonObjectChoice10.setText(names[9]);
                buttonObjectChoice11.setText(names[10]);
                buttonObjectChoice12.setText(names[11]);
                buttonObjectChoice13.setText(names[12]);
                buttonObjectChoice14.setText(names[13]);
                buttonObjectChoice15.setText(names[14]);
                buttonObjectChoice16.setText(names[15]);
                buttonObjectChoice17.setText(names[16]);
                buttonObjectChoice18.setText(names[17]);
                buttonObjectChoice19.setText(names[18]);
                buttonObjectChoice20.setText(names[19]);
                buttonObjectChoice21.setText(names[20]);
                buttonObjectChoice22.setText(names[21]);
                buttonObjectChoice23.setText(names[22]);

                linearLayout.removeView(buttonObjectChoice26);
                linearLayout.removeView(buttonObjectChoice25);
                linearLayout.removeView(buttonObjectChoice24);
                break;
            case 20:
                names = getResources().getStringArray(R.array.maths_base);

                buttonObjectChoice1.setText(names[0]);
                buttonObjectChoice2.setText(names[1]);
                buttonObjectChoice3.setText(names[2]);
                buttonObjectChoice4.setText(names[3]);
                buttonObjectChoice5.setText(names[4]);
                buttonObjectChoice6.setText(names[5]);
                buttonObjectChoice7.setText(names[6]);
                buttonObjectChoice8.setText(names[7]);
                buttonObjectChoice9.setText(names[8]);
                buttonObjectChoice10.setText(names[9]);
                buttonObjectChoice11.setText(names[10]);
                buttonObjectChoice12.setText(names[11]);
                buttonObjectChoice13.setText(names[12]);
                buttonObjectChoice14.setText(names[13]);
                buttonObjectChoice15.setText(names[14]);
                buttonObjectChoice16.setText(names[15]);
                buttonObjectChoice17.setText(names[16]);
                buttonObjectChoice18.setText(names[17]);
                buttonObjectChoice19.setText(names[18]);
                buttonObjectChoice20.setText(names[19]);

                linearLayout.removeView(buttonObjectChoice26);
                linearLayout.removeView(buttonObjectChoice25);
                linearLayout.removeView(buttonObjectChoice24);
                linearLayout.removeView(buttonObjectChoice23);
                linearLayout.removeView(buttonObjectChoice22);
                linearLayout.removeView(buttonObjectChoice21);
                break;
            case 26:
                names = getResources().getStringArray(R.array.russian);

                buttonObjectChoice1.setText(names[0]);
                buttonObjectChoice2.setText(names[1]);
                buttonObjectChoice3.setText(names[2]);
                buttonObjectChoice4.setText(names[3]);
                buttonObjectChoice5.setText(names[4]);
                buttonObjectChoice6.setText(names[5]);
                buttonObjectChoice7.setText(names[6]);
                buttonObjectChoice8.setText(names[7]);
                buttonObjectChoice9.setText(names[8]);
                buttonObjectChoice10.setText(names[9]);
                buttonObjectChoice11.setText(names[10]);
                buttonObjectChoice12.setText(names[11]);
                buttonObjectChoice13.setText(names[12]);
                buttonObjectChoice14.setText(names[13]);
                buttonObjectChoice15.setText(names[14]);
                buttonObjectChoice16.setText(names[15]);
                buttonObjectChoice17.setText(names[16]);
                buttonObjectChoice18.setText(names[17]);
                buttonObjectChoice19.setText(names[18]);
                buttonObjectChoice20.setText(names[19]);
                buttonObjectChoice21.setText(names[20]);
                buttonObjectChoice22.setText(names[21]);
                buttonObjectChoice23.setText(names[22]);
                buttonObjectChoice24.setText(names[23]);
                buttonObjectChoice25.setText(names[24]);
                buttonObjectChoice26.setText(names[25]);
                break;
        }
    }

    @Override
    public void onClick(View view) {
        Intent intent = new Intent(this, AnsweringActivity.class);

        int Number = 0;
        switch (view.getId()) {
            case R.id.buttonChoice1:
                Number = 1;
                break;
            case R.id.buttonChoice2:
                Number = 2;
                break;
            case R.id.buttonChoice3:
                Number = 3;
                break;
            case R.id.buttonChoice4:
                Number = 4;
                break;
            case R.id.buttonChoice5:
                Number = 5;
                break;
            case R.id.buttonChoice6:
                Number = 6;
                break;
            case R.id.buttonChoice7:
                Number = 7;
                break;
            case R.id.buttonChoice8:
                Number = 8;
                break;
            case R.id.buttonChoice9:
                Number = 9;
                break;
            case R.id.buttonChoice10:
                Number = 10;
                break;
            case R.id.buttonChoice11:
                Number = 11;
                break;
            case R.id.buttonChoice12:
                Number = 12;
                break;
            case R.id.buttonChoice13:
                Number = 13;
                break;
            case R.id.buttonChoice14:
                Number = 14;
                break;
            case R.id.buttonChoice15:
                Number = 15;
                break;
            case R.id.buttonChoice16:
                Number = 16;
                break;
            case R.id.buttonChoice17:
                Number = 17;
                break;
            case R.id.buttonChoice18:
                Number = 18;
                break;
            case R.id.buttonChoice19:
                Number = 19;
                break;
            case R.id.buttonChoice20:
                Number = 20;
                break;
            case R.id.buttonChoice21:
                Number = 21;
                break;
            case R.id.buttonChoice22:
                Number = 22;
                break;
            case R.id.buttonChoice23:
                Number = 23;
                break;
            case R.id.buttonChoice24:
                Number = 24;
                break;
            case R.id.buttonChoice25:
                Number = 25;
                break;
            case R.id.buttonChoice26:
                Number = 26;
                break;
        }
        intent.putExtra("subject", arguments.getString("subject"));
        Log.d("myLogs", arguments.getString("subject"));
        intent.putExtra("number", Number);
        intent.putExtra("num_of_tasks", NUM);
        startActivity(intent);
    }
}


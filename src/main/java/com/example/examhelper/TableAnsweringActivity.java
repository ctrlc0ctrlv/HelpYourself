package com.example.examhelper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TableRow;

public class TableAnsweringActivity extends AppCompatActivity implements View.OnClickListener {
    public int i = 10;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                switch (i){
                    case 10:
                        TableRow TableRow10 = findViewById(R.id.TableRow10);
                        TableRow10.removeAllViews();
                        break;
                    case 9:
                        TableRow TableRow9 = findViewById(R.id.TableRow9);
                        TableRow9.removeAllViews();
                        break;
                    case 8:
                        TableRow TableRow8 = findViewById(R.id.TableRow8);
                        TableRow8.removeAllViews();
                        break;
                    case 7:
                        TableRow TableRow7 = findViewById(R.id.TableRow7);
                        TableRow7.removeAllViews();
                        break;
                    case 6:
                        TableRow TableRow6 = findViewById(R.id.TableRow6);
                        TableRow6.removeAllViews();
                        break;
                    case 5:
                        TableRow TableRow5 = findViewById(R.id.TableRow5);
                        TableRow5.removeAllViews();
                        break;
                    case 4:
                        TableRow TableRow4 = findViewById(R.id.TableRow4);
                        TableRow4.removeAllViews();
                        break;
                    case 3:
                        TableRow TableRow3 = findViewById(R.id.TableRow3);
                        TableRow3.removeAllViews();
                        break;
                    case 2:
                        TableRow TableRow2 = findViewById(R.id.TableRow2);
                        TableRow2.removeAllViews();
                        break;
                    case 1:
                        TableRow TableRow1 = findViewById(R.id.TableRow1);
                        TableRow1.removeAllViews();
                        break;
                }
                i-=1;
                break;
        }
    }
}

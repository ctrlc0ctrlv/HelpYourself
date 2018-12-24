package com.example.examhelper;


import android.os.Bundle;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class DataBaseActivity extends AppCompatActivity implements View.OnClickListener {

    private Spinner spinner;
    private int Level = 1;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_base_set);
        spinner = findViewById(R.id.spinner);
        setupSpinner();

        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);

    }

    private void setupSpinner() {
        ArrayAdapter genderSpinnerAdapter = ArrayAdapter.createFromResource(this, R.array.levels, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(genderSpinnerAdapter);
        spinner.setSelection(2);
        spinner.setOnItemSelectedListener
                (new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                        if (selection.equals(getString(R.string.Level1))) {
                            Level = 1;
                        } else if (selection.equals(getString(R.string.Level2))) {
                            Level = 2;
                        } else if (selection.equals(getString(R.string.Level3))){
                            Level = 3;
                        }
                }
            }
            public void onNothingSelected(AdapterView<?> parent) {
                Level = 1;
            }
        });
    }

    @Override
    public void onClick(View v) {

    }
}

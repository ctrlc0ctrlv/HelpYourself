package com.easyege.examhelper;

import android.app.Fragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.easyege.examhelper.data.CustomDbHelper;

public class MainFragment2 extends Fragment {
    int Number = 1;
    String TABLE_SUBJECT_NAME = "informatics";
    TextView text_view_info;
    private SQLiteDatabase cDb;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_main2, container, false);

        CustomDbHelper cDBHelper = new CustomDbHelper(getActivity());
        cDb = cDBHelper.getReadableDatabase();
        cDBHelper.onCreate(cDb);


        FloatingActionButton fab = rootView.findViewById(R.id.fab);
        //button6.setOnClickListener(this);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DataBaseActivity.class);
                Bundle arguments = getActivity().getIntent().getExtras();
                assert arguments != null;
                //int Number = arguments.getInt("number");
                //String subject = arguments.getString("subject");
                intent.putExtra("number", Number);
                intent.putExtra("subject", TABLE_SUBJECT_NAME);
                startActivity(intent);
            }
        });

        Spinner spinner4 = rootView.findViewById(R.id.spinner4);
        ArrayAdapter genderSpinnerAdapter1 = ArrayAdapter.createFromResource(getActivity(), R.array.subjects, android.R.layout.simple_spinner_item);
        genderSpinnerAdapter1.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner4.setAdapter(genderSpinnerAdapter1);
        spinner4.setSelection(0);
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View itemSelected, int position, long id) {
                Spinner spinner3 = rootView.findViewById(R.id.spinner3);
                ArrayAdapter genderSpinnerAdapter2 = null;
                switch (position) {
                    case 0:
                        TABLE_SUBJECT_NAME = "informatics";
                        genderSpinnerAdapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.informatics, android.R.layout.simple_spinner_item);
                        break;
                    case 1:
                        TABLE_SUBJECT_NAME = "russian";
                        genderSpinnerAdapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.russian, android.R.layout.simple_spinner_item);
                        break;
                    case 2:
                        TABLE_SUBJECT_NAME = "maths_base";
                        genderSpinnerAdapter2 = ArrayAdapter.createFromResource(getActivity(), R.array.maths_base, android.R.layout.simple_spinner_item);
                        break;
                }
                assert genderSpinnerAdapter2 != null;
                genderSpinnerAdapter2.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                spinner3.setAdapter(genderSpinnerAdapter2);
                spinner3.setSelection(0);
                spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        Number = position + 1;
                        showTasks(TABLE_SUBJECT_NAME, Number);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        text_view_info = rootView.findViewById(R.id.text_view_info);
        return rootView;
    }

    void showTasks(String TABLE_SUBJECT_NAME, int Number) {
        Log.d("myLogs", "showing tasks...");
        String product = "";
        StringBuilder productBuilder1 = new StringBuilder(product);
        for (int i = 1; i < 4; i++) {
            productBuilder1.append(getString(R.string.level)).append(" ").append(i).append(":\n");
            //подключение к бд с изменяемыми заданиями
            Cursor cursor = cDb.rawQuery("SELECT * FROM " + TABLE_SUBJECT_NAME + " WHERE level ==" + i + " AND number ==" + Number, null);
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                productBuilder1.append(cursor.getInt(0)).append(". ");
                productBuilder1.append(cursor.getString(1)).append("\n");
                cursor.moveToNext();
            }
            productBuilder1.append("\n");
            cursor.close();
        }
        product = productBuilder1.toString();
        text_view_info.setText(product);
    }

    @Override
    public void onResume() {
        super.onResume();
        showTasks(TABLE_SUBJECT_NAME, Number);
    }
}

package com.easyege.examhelper;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

public class MainFragment1 extends Fragment {
    Intent intent;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootview = inflater.inflate(R.layout.fragment_main1, container, false);
        Log.d("myLogs", "exit");

        Button button = rootview.findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean done = PreferenceManager.getDefaultSharedPreferences(getActivity()).getBoolean("done_copying", false);
                RadioGroup radioGroup2 = rootview.findViewById(R.id.radioGroup2);
                switch (radioGroup2.getCheckedRadioButtonId()) {
                    case -1:
                        Toast.makeText(getActivity(), "Выберите необходимые пункты для начала работы", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioButton4:
                        if (done) {
                            intent = new Intent(getActivity(), ButtonsActivity.class);
                        } else {
                            intent = new Intent(getActivity(), ThreadActivity.class);
                        }
                        RadioGroup radioGroup = rootview.findViewById(R.id.radioGroup);
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case -1:
                                Toast.makeText(getActivity(), "Выберите необходимый предмет для начала работы", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.radioButton:
                                intent.putExtra("subject", "informatics");
                                intent.putExtra("num_of_tasks", 23);
                                startActivity(intent);
                                break;
                            case R.id.radioButton2:
                                intent.putExtra("subject", "russian");
                                intent.putExtra("num_of_tasks", 26);
                                startActivity(intent);
                                break;
                            case R.id.radioButton3:
                                intent.putExtra("subject", "maths_base");
                                intent.putExtra("num_of_tasks", 20);
                                startActivity(intent);
                                break;
                        }
                        break;
                    case R.id.radioButton5:
                        if (done) {
                            intent = new Intent(getActivity(), Test_CreatingActivity.class);
                        } else {
                            intent = new Intent(getActivity(), ThreadActivity.class);
                        }
                        radioGroup = rootview.findViewById(R.id.radioGroup);
                        switch (radioGroup.getCheckedRadioButtonId()) {
                            case -1:
                                Toast.makeText(getActivity(), "Выберите необходимый предмет для начала работы", Toast.LENGTH_LONG).show();
                                break;
                            case R.id.radioButton:
                                intent.putExtra("subject", "informatics");
                                intent.putExtra("num_of_tasks", 23);
                                startActivity(intent);
                                break;
                            case R.id.radioButton2:
                                intent.putExtra("subject", "russian");
                                intent.putExtra("num_of_tasks", 26);
                                startActivity(intent);
                                break;
                            case R.id.radioButton3:
                                intent.putExtra("subject", "maths_base");
                                intent.putExtra("num_of_tasks", 20);
                                startActivity(intent);
                                break;
                        }
                        break;
                }
            }
        });
        return rootview;
    }
}

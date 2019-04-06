package com.example.examhelper;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class FragmentsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    //TextView text_view;
    //RadioGroup radioGroup;

    final static String TAG_1 = "FRAGMENT_1";
    final static String TAG_2 = "FRAGMENT_2";
    final static String TAG_3 = "FRAGMENT_3";
    FragmentManager myFragmentManager;
    MainFragment1 myFragment1;
    MainFragment2 myFragment2;
    MainFragment3 myFragment3;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        //text_view = findViewById(R.id.text_view);
        //text_view.setText(getResources().getString(R.string.welcome_to_the_club_buddy));

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

        if (savedInstanceState == null) {
            // при первом запуске программы
            myFragmentManager = getFragmentManager();
            myFragment1 = new MainFragment1();
            myFragment2 = new MainFragment2();
            myFragment3 = new MainFragment3();

            FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
            // добавляем в контейнер при помощи метода add()
            fragmentTransaction.add(R.id.fragment1, myFragment1, TAG_1);
            fragmentTransaction.commit();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Bundle bundle = new Bundle();

        switch (menuItem.getItemId()) {
            case R.id.action_map:
                //text_view.setText("Задания");

                /*switch (radioGroup.getCheckedRadioButtonId()) {
                    case -1:
                        Toast.makeText(getBaseContext(), "Выберите необходимый предмет для начала работы", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.radioButton:
                        bundle.putString("subject", "informatics");
                        bundle.putInt("num_of_tasks", 23);
                        menuItem.setChecked(true);
                        break;
                    case R.id.radioButton2:
                        bundle.putString("subject", "russian");
                        bundle.putInt("num_of_tasks", 26);
                        menuItem.setChecked(true);
                        break;
                    case R.id.radioButton3:
                        bundle.putString("subject", "maths_base");
                        bundle.putInt("num_of_tasks", 20);
                        menuItem.setChecked(true);
                        break;
                }*/
                menuItem.setChecked(true);
                MainFragment2 fragment2 = (MainFragment2) myFragmentManager.findFragmentByTag(TAG_2);

                if (fragment2 == null) {
                    myFragment2.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                    //fragmentTransaction.remove(myFragment1);
                    fragmentTransaction.replace(R.id.fragment1, myFragment2, TAG_2);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.action_mail:
                //text_view.setText("Решать");
                menuItem.setChecked(true);

                MainFragment1 fragment1 = (MainFragment1) myFragmentManager.findFragmentByTag(TAG_1);

                if (fragment1 == null) {
                    myFragment1.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                    //fragmentTransaction.remove(myFragment2);
                    fragmentTransaction.replace(R.id.fragment1, myFragment1, TAG_1);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.action_settings:
                //text_view.setText("Настройки");
                menuItem.setChecked(true);
                MainFragment3 fragment3 = (MainFragment3) myFragmentManager.findFragmentByTag(TAG_3);

                if (fragment3 == null) {
                    myFragment3.setArguments(bundle);

                    FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                    //fragmentTransaction.remove(myFragment2);
                    fragmentTransaction.replace(R.id.fragment1, myFragment3, TAG_3);
                    fragmentTransaction.commit();
                }
        }
        return false;
    }
}

package com.example.examhelper;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

public class FragmentsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    AlertDialog.Builder ad;

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
                menuItem.setChecked(true);
                MainFragment2 fragment2 = (MainFragment2) myFragmentManager.findFragmentByTag(TAG_2);

                if (fragment2 == null) {
                    myFragment2.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment1, myFragment2, TAG_2);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.action_mail:
                menuItem.setChecked(true);
                MainFragment1 fragment1 = (MainFragment1) myFragmentManager.findFragmentByTag(TAG_1);

                if (fragment1 == null) {
                    myFragment1.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment1, myFragment1, TAG_1);
                    fragmentTransaction.commit();
                }
                break;
            case R.id.action_settings:
                menuItem.setChecked(true);
                MainFragment3 fragment3 = (MainFragment3) myFragmentManager.findFragmentByTag(TAG_3);

                if (fragment3 == null) {
                    myFragment3.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment1, myFragment3, TAG_3);
                    fragmentTransaction.commit();
                }
        }
        return false;
    }

    void createDialog() {
        //прописываем уведомление
        final Context context;
        context = FragmentsActivity.this;
        String title = "Вы уверены?";
        String message = "Выйти из приложения?";
        String yesString = "Да";
        String noString = "Отмена";
        ad = new AlertDialog.Builder(context);
        ad.setTitle(title);  // заголовок
        ad.setMessage(message); // сообщение
        ad.setCancelable(false);
        ad.setPositiveButton(yesString, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                finish();
            }
        });
        ad.setNegativeButton(noString, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            public void onClick(DialogInterface dialog, int arg1) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        Log.d("myLogs", "Back key pressed");
        createDialog();
        ad.create();
        ad.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        myFragmentManager = getFragmentManager();
        //myFragment1 = new MainFragment1();
        //myFragment2 = new MainFragment2();
        //myFragment3 = new MainFragment3();

        myFragment3.onAttach(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        myFragment3.onDetach();
    }
}

package com.example.examhelper;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.MenuItem;

public class FragmentsActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    AlertDialog.Builder ad;

    final static String TAG_1 = "FRAGMENT_1";
    final static String TAG_2 = "FRAGMENT_2";
    final static String TAG_3 = "FRAGMENT_3";
    String CURR_TAG = null;
    FragmentManager myFragmentManager;
    MainFragment1 myFragment1;
    MainFragment2 myFragment2;
    MainFragment3 myFragment3;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        String night = sharedPreferences.getString("night_mode", "Нет");
        assert night != null;
        int prev_night = AppCompatDelegate.getDefaultNightMode();
        int curr_night = 1;
        switch (night) {
            case ("Да"):
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                curr_night = 2;
                break;
            case ("Нет"):
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
        }
        if (prev_night != curr_night) {
            recreate();
        }

        int f_Size = sharedPreferences.getInt("seekBarPreference", 25);
        Log.d("myLogs", "Изменен размер текста на " + f_Size);
        Resources res = this.getResources();
        Configuration configuration = new Configuration(res.getConfiguration());
        final float start_value = 0.8f; //начальное значение размера шрифта
        //final float max_start_value = 1.6f;
        final float step = 0.016f; //шаг увеличения коэффициента
        float oldSclale = configuration.fontScale;
        float newScale = start_value + step * f_Size;
        if (oldSclale != newScale) {
            configuration.fontScale = start_value + step * f_Size;
            res.updateConfiguration(configuration, res.getDisplayMetrics());
            recreate();
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        final BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);


        myFragmentManager = getFragmentManager();
        myFragment1 = new MainFragment1();
        myFragment2 = new MainFragment2();
        myFragment3 = new MainFragment3();
        if (savedInstanceState == null) {
            // при первом запуске программы
            FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
            // добавляем в контейнер при помощи метода add()
            CURR_TAG = sharedPreferences.getString("FRAGMENT", TAG_1);
            Log.d("myLogs", CURR_TAG);
            assert CURR_TAG != null;
            // добавляем в контейнер при помощи метода add()
            switch (CURR_TAG) {
                case TAG_1:
                    fragmentTransaction.replace(R.id.fragment1, myFragment1, TAG_1);
                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
                    break;
                case TAG_2:
                    fragmentTransaction.replace(R.id.fragment1, myFragment2, TAG_2);
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                    break;
                case TAG_3:
                    fragmentTransaction.replace(R.id.fragment1, myFragment3, TAG_3);
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                    break;
            }
            fragmentTransaction.commit();
        }
        //myFragment3 = new MainFragment3();

        /*if (savedInstanceState==null) {
            myFragmentManager = getFragmentManager();
            myFragment1 = new MainFragment1();
            myFragment2 = new MainFragment2();
            myFragment3 = new MainFragment3();
            FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
            CURR_TAG = sharedPreferences.getString("FRAGMENT", TAG_1);
            Log.d("myLogs",CURR_TAG);
            assert CURR_TAG != null;
            // добавляем в контейнер при помощи метода add()
            switch (CURR_TAG) {
                case TAG_1:
                    fragmentTransaction.replace(R.id.fragment1, myFragment1, TAG_1);
                    bottomNavigationView.getMenu().getItem(1).setChecked(true);
                    break;
                case TAG_2:
                    fragmentTransaction.replace(R.id.fragment1, myFragment2, TAG_2);
                    bottomNavigationView.getMenu().getItem(0).setChecked(true);
                    break;
                case TAG_3:
                    fragmentTransaction.replace(R.id.fragment1, myFragment3, TAG_3);
                    bottomNavigationView.getMenu().getItem(2).setChecked(true);
                    break;
            }
            fragmentTransaction.commit();
        }*/
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        //Bundle bundle = new Bundle();

        switch (menuItem.getItemId()) {
            case R.id.action_map:
                menuItem.setChecked(true);
                MainFragment2 fragment2 = (MainFragment2) myFragmentManager.findFragmentByTag(TAG_2);

                if (fragment2 == null) {
                    //myFragment2.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment1, myFragment2, TAG_2);
                    fragmentTransaction.commit();
                    CURR_TAG = TAG_2;
                }
                break;
            case R.id.action_mail:
                menuItem.setChecked(true);
                MainFragment1 fragment1 = (MainFragment1) myFragmentManager.findFragmentByTag(TAG_1);

                if (fragment1 == null) {
                    //myFragment1.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment1, myFragment1, TAG_1);
                    fragmentTransaction.commit();
                    CURR_TAG = TAG_1;
                }
                break;
            case R.id.action_settings:
                menuItem.setChecked(true);
                MainFragment3 fragment3 = (MainFragment3) myFragmentManager.findFragmentByTag(TAG_3);

                if (fragment3 == null) {
                    //myFragment3.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = myFragmentManager.beginTransaction();
                    fragmentTransaction.replace(R.id.fragment1, myFragment3, TAG_3);
                    fragmentTransaction.commit();
                    CURR_TAG = TAG_3;
                    Log.d("myLogs", "Fragment 3 set");
                }
                break;
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
        myFragmentManager = getFragmentManager();
        myFragment1 = new MainFragment1();
        myFragment2 = new MainFragment2();
        myFragment3 = new MainFragment3();
        super.onResume();


        Log.d("myLogs", String.valueOf(myFragmentManager.findFragmentByTag(TAG_1)));
        Log.d("myLogs", String.valueOf(myFragmentManager.findFragmentByTag(TAG_2)));
        Log.d("myLogs", String.valueOf(myFragmentManager.findFragmentByTag(TAG_3)));
    }

    @Override
    public void onStop() {
        super.onStop();
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("FRAGMENT", CURR_TAG);
        editor.apply();
    }
}

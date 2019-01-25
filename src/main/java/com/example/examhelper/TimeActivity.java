package com.example.examhelper;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;

public class TimeActivity extends AppCompatActivity implements View.OnClickListener{
    long TIME_INTERVAL = AlarmManager.INTERVAL_DAY;
    TimePicker mTimePicker;

    private final int IDD_THREE_BUTTONS = 13;

    public static final String NEXT_NOTIFICATION = "next_notification";
    public static final String NEXT_NOTIFICATION_TIME = "next_notification_time";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        //компоненты
        mTimePicker = findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true); // формат 24 часа
        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);
        //на всякий случай выводим дату в логи, мало ли
        Date date = new Date(System.currentTimeMillis());
        Log.d("myLogs", date.toString());

        SharedPreferences activityPreferences = getSharedPreferences(NEXT_NOTIFICATION,Context.MODE_PRIVATE);
        TextView textView = findViewById(R.id.textView);
        textView.append("Ближайшее уведомление: ");
        textView.append(activityPreferences.getString(NEXT_NOTIFICATION_TIME, ""));
        /*AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, TimeNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT );
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, pendingIntent);*/
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:

                showDialog(IDD_THREE_BUTTONS);
                break;

            case R.id.button2:
                AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, TimeNotification.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT );
                manager.cancel(pendingIntent);

                Toast.makeText(this,"Уведомление удалено", Toast.LENGTH_LONG).show();

                TextView textView = findViewById(R.id.textView);
                textView.setText("");
                break;
        }

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case IDD_THREE_BUTTONS:
                final String[] mChooseIntervals = {"30 минут", "1 час", "6 часов", "12 часов", "24 часа" };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Выберите интервал появления уведомления");
                builder.setCancelable(false);
                builder.setNeutralButton("Назад",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                builder.setSingleChoiceItems(mChooseIntervals, -1,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int item) {
                                int hour = mTimePicker.getCurrentHour();
                                int minute = mTimePicker.getCurrentMinute();
                                Date date = new Date (System.currentTimeMillis());
                                Log.d("myLogs", date.toString());
                                Toast.makeText(getApplicationContext(), "Выбранный интервал: " + mChooseIntervals[item], Toast.LENGTH_SHORT).show();
                                switch (mChooseIntervals [item]){
                                    case "30 минут":
                                        TIME_INTERVAL = AlarmManager.INTERVAL_HALF_HOUR;
                                        break;
                                    case "1 час":
                                        TIME_INTERVAL = AlarmManager.INTERVAL_HOUR;
                                        break;
                                    case "6 часов":
                                        TIME_INTERVAL = AlarmManager.INTERVAL_HOUR*6;
                                        break;
                                    case "12 часов":
                                        TIME_INTERVAL = AlarmManager.INTERVAL_HALF_DAY;
                                        break;
                                    case "24 часа":
                                        TIME_INTERVAL = AlarmManager.INTERVAL_DAY;
                                        break;
                                }

                                Calendar calendar = Calendar.getInstance();
                                Calendar cal = Calendar.getInstance();
                                calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
                                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH));
                                calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH));
                                calendar.set(Calendar.HOUR_OF_DAY, hour);
                                calendar.set(Calendar.MINUTE, minute);
                                calendar.set(Calendar.SECOND, calendar.get(Calendar.SECOND));
                                calendar.set(Calendar.MILLISECOND, calendar.get(Calendar.MILLISECOND));

                                AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                                Intent intent = new Intent(TimeActivity.this, TimeNotification.class);
                                PendingIntent pendingIntent = PendingIntent.getBroadcast(TimeActivity.this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT );
                                manager.cancel(pendingIntent);
                                manager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), TIME_INTERVAL, pendingIntent);
                                Log.d("myLogs","Уведомление"+calendar.getTime().toString());


                                TextView textView = findViewById(R.id.textView);
                                textView.setText("Ближайшее уведомление: "+calendar.getTime());
                                Toast.makeText(TimeActivity.this,"Уведомление сработает: "+calendar.getTime(),Toast.LENGTH_LONG).show();

                                SharedPreferences activityPreferences = getSharedPreferences(NEXT_NOTIFICATION,Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = activityPreferences.edit();
                                editor.putString(NEXT_NOTIFICATION_TIME, calendar.getTime().toString());
                                editor.apply();
                                dialog.dismiss();
                            }
                        });
                return builder.create();
            default:
                return null;
        }
    }
}

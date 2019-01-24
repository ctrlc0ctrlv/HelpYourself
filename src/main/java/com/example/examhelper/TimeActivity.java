package com.example.examhelper;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Set;

public class TimeActivity extends AppCompatActivity implements View.OnClickListener{
    // Идентификатор уведомления
    private static final int NOTIFY_ID = 666;
    //компоненты
    private TextView mInfoTextView;
    private TimePicker mTimePicker;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        mInfoTextView = findViewById(R.id.textView);
        mTimePicker = findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true); // формат 24 часа


        Button button = findViewById(R.id.button);
        Button button2 = findViewById(R.id.button2);
        button.setOnClickListener(this);
        button2.setOnClickListener(this);

        Date date = new Date(System.currentTimeMillis());
        Log.d("myLogs", date.toString());

        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, TimeNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT );
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, pendingIntent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                Intent notificationIntent = new Intent(this, MainActivity.class);
                PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // до версии Android 8.0 API 26
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
                if (android.os.Build.VERSION.SDK_INT >= 26) {
                    NotificationChannel mChannel = new NotificationChannel("my_channel_01", "HelpYourself", NotificationManager.IMPORTANCE_DEFAULT);
                    // Configure the notification channel.
                    mChannel.setDescription("description");
                    mChannel.enableLights(true);

                    mChannel.setLightColor(Color.RED);
                    mChannel.enableVibration(true);
                    mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                    notificationManager.createNotificationChannel(mChannel);
                }
                builder.setContentIntent(contentIntent)
                        // обязательные настройки
                        .setSmallIcon(R.drawable.i_have_done)
                        //.setContentTitle(res.getString(R.string.notifytitle)) // Заголовок уведомления
                        .setContentTitle("Напоминание")
                        //.setContentText(res.getString(R.string.notifytext))
                        .setContentText("Пора за работу!!!") // Текст уведомления
                        // необязательные настройки
                        .setWhen(System.currentTimeMillis())
                        .setChannelId("my_channel_01")
                        .setAutoCancel(true); // автоматически закрыть уведомление после нажатия
                notificationManager.notify(NOTIFY_ID, builder.build());
                break;
            case R.id.button2:
                if (mTimePicker.getCurrentMinute()<10){
                    mInfoTextView.setText(new StringBuilder()
                            .append(mTimePicker.getCurrentHour()).append(":0")
                            .append(mTimePicker.getCurrentMinute()));
                }else{
                    mInfoTextView.setText(new StringBuilder()
                            .append(mTimePicker.getCurrentHour()).append(":")
                            .append(mTimePicker.getCurrentMinute()));
                }
                break;
        }

    }
}

package com.example.examhelper;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;

public class TimeActivity extends AppCompatActivity implements View.OnClickListener{

    TimePicker mTimePicker;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time);
        //компоненты

        mTimePicker = findViewById(R.id.timePicker);
        mTimePicker.setIs24HourView(true); // формат 24 часа

        Button button = findViewById(R.id.button);
        button.setOnClickListener(this);

        Date date = new Date(System.currentTimeMillis());
        Log.d("myLogs", date.toString());

        /*AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, TimeNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT );
        manager.cancel(pendingIntent);
        manager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+5000, pendingIntent);*/
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.button:
                /*Intent notificationIntent = new Intent(this, MainActivity.class);
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
                notificationManager.notify(NOTIFY_ID, builder.build());*/

                int hour = mTimePicker.getCurrentHour();
                int minute = mTimePicker.getCurrentMinute();
                Date date = new Date (System.currentTimeMillis());
                Log.d("myLogs", date.toString());

                Calendar calendar = Calendar.getInstance();
                Calendar cal = Calendar.getInstance();

                calendar.set(Calendar.YEAR, cal.get(Calendar.YEAR));
                calendar.set(Calendar.MONTH, cal.get(Calendar.MONTH));
                calendar.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH));
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, cal.get(Calendar.SECOND));
                calendar.set(Calendar.MILLISECOND, cal.get(Calendar.MILLISECOND));

                AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, TimeNotification.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT );
                manager.cancel(pendingIntent);
                manager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),86400000, pendingIntent);
                break;
        }

    }
}

package com.example.examhelper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class TimeNotification extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        boolean enabled_notifs = sharedPreferences.getBoolean("check_box_preference_1", false);
        if (enabled_notifs) {
            NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            Intent notificationIntent = new Intent(context, FragmentsActivity.class);
            PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);
            if (android.os.Build.VERSION.SDK_INT >= 26) {
                NotificationChannel mChannel = new NotificationChannel("my_channel_01", "HelpYourself", NotificationManager.IMPORTANCE_DEFAULT);
                // Configure the notification channel.
                mChannel.setDescription("Описание канала уведомлений");
                mChannel.enableLights(true);

                mChannel.setLightColor(Color.RED);
                mChannel.enableVibration(true);
                mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
                nm.createNotificationChannel(mChannel);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

            builder.setContentIntent(contentIntent);
            // обязательные настройки
            builder.setSmallIcon(R.mipmap.logo_3);
            builder.setContentTitle("Напоминание");
            builder.setContentText("Пора заняться чем-то полезным. Например зайти в приложение HelpYourself и начать готовиться к ЕГЭ =)");// Текст уведомления
            builder.setTicker("ЕГЭ уже не за горами!");
            builder.setWhen(System.currentTimeMillis());
            builder.setChannelId("my_channel_01");
            builder.setAutoCancel(true);//автоматически закрыть уведомление после нажатия
            nm.notify(1, builder.build());
            Log.d("myLogs", "Уведомление отправлено");
        }
    }
}

package com.pawan.whats_AppCleaner;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;


public class CheckRecentRun extends Service {

    private final static String TAG = "CheckRecentPlay";
    private static Long MILLISECS_PER_DAY = 86400000L;
    private static Long MILLISECS_PER_MIN = 60000L;

//    private static long delay = MILLISECS_PER_MIN * 1;   // 3 minutes (for testing)
    private static long delay = MILLISECS_PER_DAY * 3;   // 3 days
    @SuppressWarnings("FieldCanBeLocal")
    private NotificationCompat.Builder notificationBuilder;
    @SuppressWarnings("FieldCanBeLocal")
    private NotificationManager notificationManager;
    @SuppressWarnings("FieldCanBeLocal")
    private int notificationId = 0;
    @SuppressWarnings("FieldCanBeLocal")
    private String CHANNEL_ID = "my_channel_01";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.v(TAG, "Service started");
        SharedPreferences settings = getSharedPreferences(MainActivity.PREFS, MODE_PRIVATE);

        // Are notifications enabled?
        if (settings.getBoolean("enabled", true)) {
            // Is it time for a notification?
            if (settings.getLong("lastRun", Long.MAX_VALUE) < System.currentTimeMillis() - delay)
                sendNotification();

        } else {
            Log.i(TAG, "Notifications are disabled");
        }

        // Set an alarm for the next time this service should run:
        setAlarm();

        Log.v(TAG, "Service stopped");
        stopSelf();
    }

    public void setAlarm() {

        Intent serviceIntent = new Intent(this, CheckRecentRun.class);
        PendingIntent pi = PendingIntent.getService(this, 131313, serviceIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        am.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + delay, pi);
        Log.v(TAG, "Alarm set");
    }

    public void sendNotification() {
        Intent mainIntent = new Intent(this, MainActivity.class);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Notifications";// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            mChannel.setSound(null, null);
            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentIntent(PendingIntent.getActivity(this, 131314, mainIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentTitle("We Miss You!")
                    .setContentText("Please clear your WhatsApp Media Clutter.")
                    .setTicker("We Miss You! Please Clear Your WhatsApp Media Clutter.")
                    .setChannelId(CHANNEL_ID)
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .setSound(null)
                    .setLights(Color.GREEN, 3000, 3000)
                    .setColor(Color.parseColor("#12921F"))
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setAutoCancel(true);

            notificationManager.createNotificationChannel(mChannel);
            notificationManager.notify(notificationId, notificationBuilder.build());  Log.v(TAG, "Notification sent");

        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentIntent(PendingIntent.getActivity(this, 131314, mainIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentTitle("We Miss You!")
                    .setContentText("Please clear your WhatsApp Media Clutter.")
                    .setTicker("We Miss You! Please Clear Your WhatsApp Media Clutter.")
                    .setPriority(NotificationManager.IMPORTANCE_DEFAULT)
                    .setSound(null)
                    .setLights(Color.GREEN, 3000, 3000)
                    .setColor(Color.parseColor("#12921F"))
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setAutoCancel(true);
            notificationManager.notify(notificationId, notificationBuilder.build());
            Log.v(TAG, "Notification sent");
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
            notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                    .setSmallIcon(R.drawable.notification_icon)
                    .setContentIntent(PendingIntent.getActivity(this, 131314, mainIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT))
                    .setContentTitle("We Miss You!")
                    .setContentText("Please clear your WhatsApp Media Clutter.")
                    .setTicker("We Miss You! Please Clear Your WhatsApp Media Clutter.")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .setSound(null)
                    .setLights(Color.GREEN, 3000, 3000)
                    .setColor(Color.parseColor("#12921F"))
                    .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
                    .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                    .setAutoCancel(true);
            notificationManager.notify(notificationId, notificationBuilder.build());
            Log.v(TAG, "Notification sent");
        }



    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
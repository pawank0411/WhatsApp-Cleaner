package com.pawan.whatsAppCleaner;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
public class CheckRecentRun extends Service {

    private final static String TAG = "CheckRecentPlay";
    private static Long MILLISECS_PER_DAY = 86400000L;
    private static Long MILLISECS_PER_MIN = 60000L;

//      private static long delay = MILLISECS_PER_MIN * 3;   // 3 minutes (for testing)
    private static long delay = MILLISECS_PER_DAY * 3;   // 3 days

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
        @SuppressWarnings("deprecation")
        Notification noti = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentIntent(PendingIntent.getActivity(this, 131314, mainIntent,
                        PendingIntent.FLAG_UPDATE_CURRENT))
                .setContentTitle("We Miss You!")
                .setContentText("Please clear your WhatsApp Media Clutter.")
                .setDefaults(Notification.DEFAULT_ALL)
                .setSmallIcon(R.drawable.ic_icon)
                .setTicker("We Miss You! Please Clear Your WhatsApp Media Clutter.")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .getNotification();

        NotificationManager notificationManager
                = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(131315, noti);

        Log.v(TAG, "Notification sent");
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
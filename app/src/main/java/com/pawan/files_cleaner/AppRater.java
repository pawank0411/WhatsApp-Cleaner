package com.pawan.files_cleaner;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

public class AppRater {
    private final static int DAYS_UNTIL_PROMPT = 3;//Min number of days
    private final static int LAUNCHES_UNTIL_PROMPT = 3;//Min number of launches

    private final static String APP_TITLE = "whats-App cleaner";// App Name
    private final static String APP_PNAME = "com.pawan.files_cleaner";// Package Name
    public static void app_launched(final Context mContext) {

        SharedPreferences prefs = mContext.getSharedPreferences("apprater", 0);
        if (prefs.getBoolean("dontshowagain", false)) {
            return;
        }

        final SharedPreferences.Editor editor = prefs.edit();

        // Increment launch counter
        long launch_count = prefs.getLong("launch_count", 0) + 1;
        editor.putLong("launch_count", launch_count);

        // Get date of first launch
        long date_firstLaunch = prefs.getLong("date_firstlaunch", 0);
        if (date_firstLaunch == 0) {
            date_firstLaunch = System.currentTimeMillis();
            editor.putLong("date_firstlaunch", date_firstLaunch);
        }

        // Wait at least n days before opening
        if (launch_count >= LAUNCHES_UNTIL_PROMPT) {
            if (System.currentTimeMillis() >= date_firstLaunch +
                    (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)) {
                Log.e("launch", "Succes");
                new AlertDialog.Builder(mContext)
                        .setMessage("If you enjoy using " + APP_TITLE + ", please take a moment to Rate it. Thanks for your support!")
                        .setCancelable(false)
                        .setPositiveButton("Rate Us", (dialog, which) -> mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + APP_PNAME))))
                        .setNegativeButton("No, Thanks", (dialog, which) -> {
                            editor.putBoolean("dontshowagain", true);
                            editor.apply();

                            dialog.dismiss();
                        })
                        .setNeutralButton("Remind Me Later", (dialog, which) -> dialog.dismiss())
                        .create().show();

            }
        }

        editor.commit();
    }
}

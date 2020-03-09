package com.pawan.files_cleaner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class Noapp extends AppCompatActivity {
    private AdView mAdView;
    private ProgressDialog progressDialog;
    private final static String TAG = "MainActivity";
    public final static String PREFS = "PrefsFile";
    @SuppressWarnings("FieldCanBeLocal")
    private SharedPreferences settings = null;
    @SuppressWarnings("FieldCanBeLocal")
    private SharedPreferences.Editor editor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.no_app);

        SharedPreferences sharedPreferences = this.getSharedPreferences("Network",0);
        boolean status = sharedPreferences.getBoolean("Status",false);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait");
        progressDialog.setCancelable(false);
        progressDialog.show();

        settings = getSharedPreferences(PREFS, MODE_PRIVATE);


        // First time running app?
        if (!settings.contains("lastRun"))
            enableNotification(null);
        else
            recordRunTime();

        Log.v(TAG, "Starting CheckRecentRun service...");
        startService(new Intent(this, CheckRecentRun.class));

        if (!status) {
            progressDialog.dismiss();
            Toast.makeText(Noapp.this, "Please Install WhatsApp Messenger", Toast.LENGTH_LONG).show();
            finishAffinity();

        } else {
            mAdView = findViewById(R.id.adView);
            mAdView.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A").build());

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    progressDialog.dismiss();
                    if (!mAdView.isLoading()) {
                        mAdView.loadAd(new AdRequest.Builder().addTestDevice("623B1B7759D51209294A77125459D9B7").addTestDevice("C07AF1687B80C3A74C718498EF9B938A").build());
                    }
                    finishAffinity();
                }

                @Override
                public void onAdLoaded() {
                    Log.e("Banner", "Loaded");
                    progressDialog.dismiss();
                    Toast.makeText(Noapp.this, "Please Install WhatsApp Messenger", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onAdFailedToLoad(int i) {
                    Log.e("Bannercode", String.valueOf(i));
                    progressDialog.dismiss();
                    Toast.makeText(Noapp.this, "Please Install WhatsApp Messenger", Toast.LENGTH_LONG).show();
                    finishAffinity();
                }
            });

        }
    }
    public void recordRunTime() {
        editor = settings.edit();
        editor.putLong("lastRun", System.currentTimeMillis());
        editor.apply();
    }

    public void enableNotification(View v) {
        editor = settings.edit();
        editor.putLong("lastRun", System.currentTimeMillis());
        editor.putBoolean("enabled", true);
        editor.apply();
        Log.v(TAG, "Notifications enabled");
    }


    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}



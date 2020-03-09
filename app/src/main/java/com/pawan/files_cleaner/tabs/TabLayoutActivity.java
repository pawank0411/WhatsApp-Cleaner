package com.pawan.files_cleaner.tabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pawan.files_cleaner.CheckRecentRun;
import com.pawan.files_cleaner.DataHolder;
import com.pawan.files_cleaner.R;

public class TabLayoutActivity extends AppCompatActivity {

    private final static String TAG = "MainActivity";
    public final static String PREFS = "PrefsFile";
    @SuppressWarnings("FieldCanBeLocal")
    private SharedPreferences settings = null;
    @SuppressWarnings("FieldCanBeLocal")
    private SharedPreferences.Editor editor = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.viewpager);
        String category = getIntent().getStringExtra("category");
        String pathname = getIntent().getStringExtra("pathname");
        TabsAdapter tabsAdapter;

        settings = getSharedPreferences(PREFS, MODE_PRIVATE);
        editor = settings.edit();


        // First time running app?
        if (!settings.contains("lastRun"))
            enableNotification(null);
        else
            recordRunTime();

        Log.v(TAG, "Starting CheckRecentRun service...");
        startService(new Intent(this, CheckRecentRun.class));

        switch (category) {

            case DataHolder.IMAGE:
                tabsAdapter = new TabsAdapter(getSupportFragmentManager(), DataHolder.IMAGE, DataHolder.imagesReceivedPath, DataHolder.imagesSentPath);
                break;
            case DataHolder.DOCUMENT:
                tabsAdapter = new TabsAdapter(getSupportFragmentManager(), DataHolder.DOCUMENT, DataHolder.documentsReceivedPath, DataHolder.documentsSentPath);
                break;
            case DataHolder.VIDEO:
                tabsAdapter = new TabsAdapter(getSupportFragmentManager(), DataHolder.VIDEO, DataHolder.videosReceivedPath, DataHolder.videosSentPath);
                break;
            case DataHolder.AUDIO:
                tabsAdapter = new TabsAdapter(getSupportFragmentManager(), DataHolder.AUDIO, DataHolder.audiosReceivedPath, DataHolder.audiosSentPath);
                break;
            case DataHolder.GIF:
                tabsAdapter = new TabsAdapter(getSupportFragmentManager(), DataHolder.GIF, DataHolder.gifReceivedPath, DataHolder.gifSentPath);
                break;
            case DataHolder.WALLPAPER:
                tabsAdapter = new TabsAdapter(getSupportFragmentManager(), DataHolder.WALLPAPER, DataHolder.wallReceivedPath, DataHolder.wallgifSentPath);
                break;
            case DataHolder.VOICE:
                tabsAdapter = new TabsAdapter(getSupportFragmentManager(), DataHolder.VOICE, DataHolder.voiceReceivedPath, DataHolder.voicegifSentPath);
                break;
            case DataHolder.STATUS:
                tabsAdapter = new TabsAdapter(getSupportFragmentManager(), DataHolder.STATUS, DataHolder.statuscache, DataHolder.statusdownload);
                break;
            default:
                tabsAdapter = new TabsAdapter(getSupportFragmentManager(), DataHolder.NONDEFAULT, pathname, pathname);
                break;
        }

        viewPager.setAdapter(tabsAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }
    public void recordRunTime() {
        editor.putLong("lastRun", System.currentTimeMillis());
        editor.commit();
    }

    public void enableNotification(View v) {
        editor.putLong("lastRun", System.currentTimeMillis());
        editor.putBoolean("enabled", true);
        editor.commit();
        Log.v(TAG, "Notifications enabled");
    }
}


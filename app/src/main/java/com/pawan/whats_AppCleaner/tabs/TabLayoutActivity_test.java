package com.pawan.whats_AppCleaner.tabs;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.pawan.whats_AppCleaner.CheckRecentRun;
import com.pawan.whats_AppCleaner.DataHolder;
import com.pawan.whats_AppCleaner.R;

public class TabLayoutActivity_test extends AppCompatActivity {
    private static OnTabListener mOnTabListener;
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
        TabsAdapter_test tabsAdapter_test;

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
            default:
            case DataHolder.STATUS:
                tabsAdapter_test = new TabsAdapter_test(getSupportFragmentManager(), DataHolder.STATUS, DataHolder.statuscache, DataHolder.statusdownload);
                break;
        }

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (mOnTabListener != null){
                    mOnTabListener.ontablistener(tab); }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        viewPager.setAdapter(tabsAdapter_test);

        tabLayout.setupWithViewPager(viewPager);
    }
    public static void setmOnTabListener(OnTabListener onTabListener) {
        mOnTabListener = onTabListener;
    }


    public interface OnTabListener {
        void ontablistener(TabLayout.Tab tab);
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


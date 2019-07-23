package com.pawan.whatsAppCleaner.tabs;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.pawan.whatsAppCleaner.DataHolder;
import com.pawan.whatsAppCleaner.R;

public class TabLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.viewpager);
        String category = getIntent().getStringExtra("category");
        TabsAdapter tabsAdapter;

        switch (category) {
            default:
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
        }

        viewPager.setAdapter(tabsAdapter);

        tabLayout.setupWithViewPager(viewPager);
    }
}


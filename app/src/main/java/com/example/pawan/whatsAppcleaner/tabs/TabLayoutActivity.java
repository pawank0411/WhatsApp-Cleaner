package com.example.pawan.whatsAppcleaner.tabs;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.example.pawan.whatsAppcleaner.DataHolder;
import com.example.pawan.whatsAppcleaner.R;

public class TabLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

        TabLayout tabLayout = findViewById(R.id.tabs);
        ViewPager viewPager = findViewById(R.id.viewpager);
        tabLayout.setSelectedTabIndicatorHeight(0);

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
        }
        viewPager.setAdapter(tabsAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}


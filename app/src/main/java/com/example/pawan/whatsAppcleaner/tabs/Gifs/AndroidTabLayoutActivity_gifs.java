package com.example.pawan.whatsAppcleaner.tabs.Gifs;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.pawan.whatsAppcleaner.R;

public class AndroidTabLayoutActivity_gifs extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

        TabHost tabHost = getTabHost();
        tabHost.getTabWidget().setStripEnabled(false);
        tabHost.setFocusableInTouchMode(false);

        TabHost.TabSpec recived_video = tabHost.newTabSpec("Photos");
        recived_video.setIndicator("Received");

        Intent recvd = new Intent(AndroidTabLayoutActivity_gifs.this, Gifs_rec.class);
        recived_video.setContent(recvd);


        TabHost.TabSpec sent_video = tabHost.newTabSpec("Sent");
        sent_video.setIndicator("Sent");

        Intent snt = new Intent(AndroidTabLayoutActivity_gifs.this, Gifs_sent.class);
        sent_video.setContent(snt);

        tabHost.addTab(recived_video);
        tabHost.addTab(sent_video);
    }

}


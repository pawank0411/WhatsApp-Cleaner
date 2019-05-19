package com.example.pawan.whatsAppcleaner.tabs.Audio;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.pawan.whatsAppcleaner.R;

public class AndroidTabLayoutActivity_aud extends TabActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);


        TabHost tabHost = getTabHost();
        tabHost.getTabWidget().setStripEnabled(false);



        TabHost.TabSpec recived_aud = tabHost.newTabSpec("Audios");
        recived_aud.setIndicator("Received");

        Intent recvd = new Intent(AndroidTabLayoutActivity_aud.this, Aud_rec.class);
        recived_aud.setContent(recvd);


        TabHost.TabSpec sent_aud = tabHost.newTabSpec("Sent");
        sent_aud.setIndicator("Sent");

        Intent snt = new Intent(AndroidTabLayoutActivity_aud.this, Aud_sent.class);
        sent_aud.setContent(snt);

        tabHost.addTab(recived_aud);
        tabHost.addTab(sent_aud);
    }

}


package com.example.pawan.whatsAppcleaner.tabs.Images;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.pawan.whatsAppcleaner.R;

public class AndroidTabLayoutActivity_img extends TabActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);

        TabHost tabHost = getTabHost();



        TabHost.TabSpec recived_img = tabHost.newTabSpec("Photos");
        recived_img.setIndicator("Received");

        Intent recvd = new Intent(AndroidTabLayoutActivity_img.this, Images_rec.class);
        recived_img.setContent(recvd);


        TabHost.TabSpec sent_img = tabHost.newTabSpec("Sent");
        sent_img.setIndicator("Sent");

        Intent snt = new Intent(AndroidTabLayoutActivity_img.this, Images_sent.class);
        sent_img.setContent(snt);

        tabHost.addTab(recived_img);
        tabHost.addTab(sent_img);
    }


}


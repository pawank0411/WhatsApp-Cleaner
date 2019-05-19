package com.example.pawan.whatsAppcleaner.tabs.Documents;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;

import com.example.pawan.whatsAppcleaner.R;

public class AndroidTabLayoutActivity_doc extends TabActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab);


        TabHost tabHost = getTabHost();
        tabHost.getTabWidget().setStripEnabled(false);



        TabHost.TabSpec recived_doc = tabHost.newTabSpec("Photos");
        recived_doc.setIndicator("Received");

        Intent recvd = new Intent(AndroidTabLayoutActivity_doc.this, Doc_rec.class);
        recived_doc.setContent(recvd);


        TabHost.TabSpec sent_doc = tabHost.newTabSpec("Sent");
        sent_doc.setIndicator("Sent");

        Intent snt = new Intent(AndroidTabLayoutActivity_doc.this, Doc_sent.class);
        sent_doc.setContent(snt);

        tabHost.addTab(recived_doc);
        tabHost.addTab(sent_doc);
    }

}


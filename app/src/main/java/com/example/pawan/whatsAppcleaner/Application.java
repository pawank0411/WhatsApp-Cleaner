package com.example.pawan.whatsAppcleaner;

import com.google.android.gms.ads.MobileAds;

public class Application extends android.app.Application {

    private static final String AD_ID = "ca-app-pub-7255339257613393~8837303265";

    @Override
    public void onCreate() {
        super.onCreate();

        MobileAds.initialize(this,
                AD_ID);
    }
}

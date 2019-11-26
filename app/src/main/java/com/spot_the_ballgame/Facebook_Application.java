package com.spot_the_ballgame;

import android.app.Application;

import com.facebook.ads.AudienceNetworkAds;

public class Facebook_Application extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AudienceNetworkAds.initialize(this);
    }
}

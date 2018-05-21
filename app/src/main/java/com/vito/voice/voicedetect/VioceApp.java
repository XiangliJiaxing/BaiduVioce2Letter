package com.vito.voice.voicedetect;

import android.app.Application;

public class VioceApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        ContextRefUtil.init(this);
    }
}

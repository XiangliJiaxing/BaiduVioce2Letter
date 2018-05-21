package com.vito.voice.voicedetect;

import android.content.Context;

/**
 * Created by vito-xa49 on 2017/9/29.
 *
 */

public class ContextRefUtil {
    private static Context appContext;

    public static final void init(Context context){
        appContext = context;
    }

    public static Context getAppContext(){
        return appContext;
    }
}

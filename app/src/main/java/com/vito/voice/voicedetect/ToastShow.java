package com.vito.voice.voicedetect;

import android.app.Application;
import android.widget.Toast;


public class ToastShow {
    public static final int LENGTH_SHORT = 0;
    public static final int LENGTH_LONG = 1;
    private static Toast toast = null;
    private static Application mApplication = null;

    public static void init(Application application) {
        mApplication = application;
    }

    public static void toastShow(String text, int durtime) {
        if (toast == null) {
            toast = Toast.makeText(mApplication.getApplicationContext(), text, durtime);
        } else {
            toast.setText(text);
            toast.setDuration(durtime);
        }
        toast.show();
    }

    public static void toastShow(int str_id, int durtime) {
        if (toast == null) {
            toast = Toast.makeText(mApplication.getApplicationContext(), str_id, durtime);
        } else {
            toast.setText(str_id);
            toast.setDuration(durtime);
        }
        toast.show();
    }

    public static void toastLong(String text) {
        toastShow(text, Toast.LENGTH_LONG);
    }

    public static void toastLong(int str_id) {
        toastShow(str_id, Toast.LENGTH_LONG);
    }

    public static void toastShort(String text) {
        toastShow(text, Toast.LENGTH_SHORT);
    }

    public static void toastShort(int str_id) {
        toastShow(str_id, Toast.LENGTH_SHORT);
    }
}
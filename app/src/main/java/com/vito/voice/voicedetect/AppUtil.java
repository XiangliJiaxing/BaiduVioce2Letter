package com.vito.voice.voicedetect;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.Display;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by vito-xa49 on 2017/9/13.
 */

public class AppUtil {

    /**
     * 重启应用的Acriivty
     */
    public static void restartAppByActivity() {
        Context appContext = ContextRefUtil.getAppContext();
        Intent i = appContext.getPackageManager().getLaunchIntentForPackage(appContext.getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);//modify code by zyc 2015/11/16 Note:Change the boot mode
        appContext.startActivity(i);
    }

    public static void restartApp(Class clazz) {
        Context appContext = ContextRefUtil.getAppContext();
//        System.exit(0);
        String command = "kill -9 " + android.os.Process.myPid();
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            Intent intent2 = new Intent(appContext, clazz);
            PendingIntent restartIntent = PendingIntent.getActivity(appContext, 0, intent2, Intent.FLAG_ACTIVITY_NEW_TASK);
            //退出程序
            AlarmManager mgr = (AlarmManager) appContext.getSystemService(Context.ALARM_SERVICE);
            mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 1, restartIntent);
        }
    }

    public static void kill() {
        String command = "kill -9 " + android.os.Process.myPid();
        try {
            Runtime.getRuntime().exec(command);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
    }

    /**
     * 安装应用
     *
     * 安装包不完整---解析异常
     * @param context
     * @param apkPath
     */
    public static void installApk(Context context, String apkPath) {
        Uri uri = FileUtils.getFileUri(apkPath);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setDataAndType(uri,"application/vnd.android.package-archive");
        context.startActivity(intent);
        android.os.Process.killProcess(android.os.Process.myPid()); // 提示完成
    }


    /**
     * 判断指定的intent能否被当前手机解析
     *
     * @param intent
     * @param context
     * @return
     */
    public static boolean isIntentAvailable(Intent intent, Context context) {
        if (intent == null) {
            return false;
        }
        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY).size() > 0;
    }

//    @TargetApi(23)
//    public static boolean isIntentAvailable(Context context, Intent intent){
//        if (intent == null) {
//            return false;
//        }
//        return context.getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_ALL).size() > 0;
//    }


    /**
     * 获取虚拟导航栏的高度
     *
     * @param context
     * @return
     */
    public static float getVirtualNavigationBarHeight(@NonNull Activity context) {

        // 1.获取不含虚拟键的高度
        float heightWithOutKey = context.getWindowManager().getDefaultDisplay().getHeight();

        // 2.获取包含虚拟键的屏幕高度
//        context.getWindowManager().getDefaultDisplay().getRealMetrics(new DisplayMetrics());
        float heightWithKey = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = context.getWindowManager().getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            Class c;
            try {
                c = Class.forName("android.view.Display");
                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, metrics);
                heightWithKey = metrics.heightPixels;
            } catch (ClassNotFoundException ex) {
                ex.printStackTrace();
            } catch (InvocationTargetException ex) {
                ex.printStackTrace();
            } catch (IllegalAccessException ex) {
                ex.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        } else {
            heightWithKey = 0;
        }
        // 3.---
        float resultHeight = heightWithKey - heightWithOutKey;
        if (resultHeight <= 0) {
            resultHeight = 0;
        }
        return resultHeight;
    }

}

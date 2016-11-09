package com.heneng.heater.utils;

import android.util.Log;

/**
 * Created by Administrator on 2015/9/18.
 */
public class LogUtils {
    private static boolean enableLog = true;

    public static void e(String msg) {
        if (enableLog) {
            Log.e("TAG", msg);
        }

    }
}

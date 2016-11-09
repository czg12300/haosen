package com.heneng.heater.lastcoder.common;

//禁止过快点击
public class CheckFastClick {
    private static long lastClickTime;

    public static boolean IsFastClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

}

package com.heneng.heater.views;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.TextView;

import com.heneng.heater.R;

/**
 * TODO 自定义全屏加载Dialog
 *
 * @author HuangYiHao
 * @data: 2014年10月25日 上午9:48:53
 * @version: V1.0
 * //                              _oo0oo_
 * //                             o8888888o
 * //                             88" . "88
 * //                             (| -_- |)
 * //                             0\  =  /0
 * //                           ___/'---'\___
 * //                        .' \\\|     |// '.
 * //                       / \\\|||  :  |||// \\
 * //                      / _ ||||| -:- |||||- \\
 * //                      | |  \\\\  -  /// |   |
 * //                      | \_|  ''\---/''  |_/ |
 * //                      \  .-\__  '-'  __/-.  /
 * //                    ___'. .'  /--.--\  '. .'___
 * //                 ."" '<  '.___\_<|>_/___.' >'  "".
 * //                | | : '-  \'.;'\ _ /';.'/ - ' : | |
 * //                \  \ '_.   \_ __\ /__ _/   .-' /  /
 * //            ====='-.____'.___ \_____/___.-'____.-'=====
 * //                              '=---='
 * //
 * //          ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * //
 * //                  佛祖保佑                 永无BUG       永不修改
 * //
 */
public class LoadingDialog extends Dialog {

    private Animation rotAnim;
    private TextView tv_info;

    public LoadingDialog(Context context) {
        super(context);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.a1_common_loading);
        tv_info = (TextView) findViewById(R.id.common_loading_tv_msg);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }



    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            dismiss();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}

package com.heneng.heater.activitys;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.AppAplication;
import com.heneng.heater.lastcoder.LoadingFragment;
import com.heneng.heater.views.HeaderView;
import com.heneng.heater.views.LoadingDialog;


import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/9/18.
 */
public abstract class BaseActivity extends Activity {

    public BaseActivity mBaseActivity;
    @InjectView(R.id.header)
    public HeaderView header;
    private LoadingDialog _loadingDialog;
    public Handler _handler = new Handler();
    public SharedPreferences _sp;
    public AppAplication _app;

    /**
     * 初始化视图方法，如果没特殊需求，建议在此方法中初始化视图，不要重写onCreate
     *
     * @param savedInstanceState
     */
    public abstract void init(Bundle savedInstanceState);


    public void showLoading() {
        if (_loadingDialog == null) {
            _loadingDialog = new LoadingDialog(this);
            _loadingDialog.setCancelable(false);
            _loadingDialog.show();

        }
    }

    public void closeLoading() {
        if (_loadingDialog != null) {
            _loadingDialog.dismiss();
            _loadingDialog = null;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _sp = getSharedPreferences("uinfo", Context.MODE_PRIVATE);
        mBaseActivity = this;
        _app = (AppAplication) getApplication();
        init(savedInstanceState);
        if (header.getLeftListener() == null) {
            header.setLeftListener(new HeaderView.onClickLeftListener() {
                @Override
                public void onLeftClick(View view) {
                    mBaseActivity.finish();
                }
            });
        }
    }


    public void openActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        startActivity(intent);
    }

    public void openActivityWithParams(Class<? extends Activity> clazz, Bundle extra) {
        Intent intent = new Intent();
        intent.setClass(this, clazz);
        intent.putExtras(extra);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.reset(this);
    }
}

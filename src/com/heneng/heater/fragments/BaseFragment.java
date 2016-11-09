package com.heneng.heater.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.heneng.heater.lastcoder.AppAplication;
import com.heneng.heater.views.LoadingDialog;

import java.util.Map;

/**
 * Created by Yihao Huang on 2015/9/18.
 */
public class BaseFragment extends Fragment {
    public Activity mContext;
    public SharedPreferences _sp;
    public AppAplication _app;
    private LoadingDialog _loadingDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        _sp = mContext.getSharedPreferences("uinfo", Context.MODE_PRIVATE);
        _app = (AppAplication) mContext.getApplication();
    }

    public void openActivity(Class<? extends Activity> clazz) {
        Intent intent = new Intent();
        intent.setClass(mContext, clazz);
        mContext.startActivity(intent);
    }

    public void openActivityWithParams(Class<? extends Activity> clazz, Bundle extra) {
        Intent intent = new Intent();
        intent.setClass(mContext, clazz);
        intent.putExtras(extra);
        mContext.startActivity(intent);
    }


    public  boolean isLoading(){
        return  _loadingDialog!=null;
    }


    public void showLoading() {
        if (_loadingDialog == null) {
            _loadingDialog = new LoadingDialog(mContext);
            _loadingDialog.setCancelable(false);
        }
        _loadingDialog.show();
    }

    public void closeLoading() {
        if (_loadingDialog != null) {
            _loadingDialog.dismiss();
            _loadingDialog = null;
        }
    }


}

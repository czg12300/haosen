package com.heneng.heater.activitys;

import android.os.Bundle;

import com.heneng.heater.utils.NetUtils;
import com.heneng.heater.views.LoadingDialog;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/20.
 */
public abstract class BaseNetActivity extends BaseActivity implements NetUtils.NetCallbackListener {


    public Map<String, Object> getParamsMap() {
        return new HashMap<>();
    }

    @Override
    protected void onPause() {
        super.onPause();
        NetUtils.unregistCallbackListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetUtils.registNetCallbackListener(this);
    }

}

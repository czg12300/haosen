package com.heneng.heater.fragments;

import com.heneng.heater.utils.NetUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2015/9/21.
 */
public abstract class BaseNetFragment extends BaseFragment implements NetUtils.NetCallbackListener {
    public Map<String, Object> getParamsMap() {
        return new HashMap<>();
    }


    @Override
    public void onResume() {
        super.onResume();
        NetUtils.registNetCallbackListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        NetUtils.unregistCallbackListener(this);
    }


}

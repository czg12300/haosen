package com.heneng.heater.lastcoder;

import android.app.Application;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.heneng.heater.lastcoder.common.GetRemoteData;
import com.heneng.heater.lastcoder.model.AppParams;
import com.heneng.heater.lastcoder.model.PostResult;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.ResultDetail;
import com.heneng.heater.models.ResultDeviceList;
import com.heneng.heater.utils.NetUtils;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by KUANGJH on 2015-8-10.
 */
public class AppAplication extends Application {

    Timer timer;
    public List<ResultDeviceList.Device> _deviceList = new ArrayList<>();

    public ResultDeviceList.Device _chooseDevice;

    //用于详细界面点击故障提交跳转到故障页
    public boolean _isJumpToReapirFragment;


    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * session保持
     */
    public void handleLoginHeart() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
//                new AsyncTask<Void, Void, PostResult>() {
//
//                    @Override
//                    protected PostResult doInBackground(Void... params) {
//                        String strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=LoginHeartbeat&uc=" + UserInfo.UserCode;
//                        String jstr = GetRemoteData.ConnServerForResult(strUrl, getBaseContext());
//                        Log.d("LoginHeartbeat", "response -> " + jstr);
//                        if (!TextUtils.isEmpty(jstr)) {
//                            PostResult result = new Gson().fromJson(jstr, PostResult.class);
//                            if (result.Result.equals("1")) {
//                                Log.d("LoginHeartbeat", "续期成功");
//                            } else {
//                                Log.d("LoginHeartbeat", "续期失败");
//                            }
//                        }
//                        return null;
//                    }
//
//                }.execute();
                Map<String, Object> params = new HashMap<String, Object>();
                params.put("uc", UserInfo.UserCode);

                NetUtils.executGet(getBaseContext(), 1000000, "LoginHeartbeat", params, ResultDetail.class);

            }
        }, 60 * 1000, 60 * 10000);
    }
}

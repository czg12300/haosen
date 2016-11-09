package com.heneng.heater.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.LoginActivity;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultCommon;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.NetUtils;
import com.heneng.heater.views.HeaderView;
import com.heneng.heater.views.SettingItem;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/9/18.
 */
public class AccountActivity extends BaseNetActivity {



    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.a1_activity_accounts);
        ButterKnife.inject(this);
    }


    @OnClick(R.id.account_item_reset_psw)
    public void toResetPsw() {
        openActivity(ResetPswActivity.class);

    }



    @OnClick(R.id.account_logout)
    public void toLogout() {
        Map<String, Object> params = getParamsMap();
        params.put("uc", UserInfo.UserCode);
        NetUtils.executPost(mBaseActivity,Constants.CODE_GET_LOGOUT, "SystemLoginout", params, ResultCommon.class);
        showLoading();
    }


    @Override
    public void callback(int code, BaseModel model) {
        if (code == Constants.CODE_GET_LOGOUT) {
            closeLoading();
            ResultCommon result = (ResultCommon) model;
            if (Constants.RESULT_YES.equals(result.getResult())) {
                Toast.makeText(mBaseActivity, "注销登录成功", Toast.LENGTH_SHORT).show();
                openActivity(LoginActivity.class);
                finish();
            } else {
                Toast.makeText(mBaseActivity, "注销登录失败", Toast.LENGTH_SHORT).show();
            }
        }
    }
}

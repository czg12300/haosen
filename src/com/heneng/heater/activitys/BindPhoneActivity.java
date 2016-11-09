package com.heneng.heater.activitys;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.heneng.heater.R;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultCommon;
import com.heneng.heater.models.ParamUser;
import com.heneng.heater.models.ResultDetail;
import com.heneng.heater.utils.CommonUtils;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.NetUtils;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/9/21.
 */
public class BindPhoneActivity extends BaseNetActivity {
    @InjectView(R.id.bind_phone_et_phone)
    EditText et_phone;
    @InjectView(R.id.bind_phone_et_code)
    EditText et_code;
    @InjectView(R.id.bind_phone_tv_get_code)
    TextView tv_get_code;
    private int _timeCount = 61;

    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.a1_activity_bind_phone);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.bind_phone_tv_get_code)
    void getCode() {
        CharSequence phoneNo = checkPhone();
        if (phoneNo == null) return;

        HashMap<String, Object> params = new HashMap<>();
        params.put("phone", phoneNo);

        NetUtils.executGet(mBaseActivity,Constants.CODE_GET_CODE, "GetMsgValidateCode", params, ResultCommon.class);

        timeCount();
    }

    private void resetCodeStatus() {
        tv_get_code.setText("获取验证码");
        _timeCount = 61;
        tv_get_code.setEnabled(true);
    }

    void timeCount() {
        _timeCount -= 1;
        tv_get_code.setEnabled(false);
        if (_timeCount >= 0) {
            tv_get_code.setText(_timeCount + "S");
            if (_timeCount == 0) {
                resetCodeStatus();
            } else {
                _handler.postDelayed(_timeCountRunable, 1000);
            }
        }
    }

    Runnable _timeCountRunable = new Runnable() {
        @Override
        public void run() {
            timeCount();
        }

    };

    @OnClick(R.id.bind_phone_tv_next)
    void toNext() {
        CharSequence code = et_code.getText();
        CharSequence phoneNo = checkPhone();
        if (phoneNo == null) {
            return;
        }

        if (TextUtils.isEmpty(code)) {
            Toast.makeText(mBaseActivity, "请输入短信验证码", Toast.LENGTH_SHORT).show();
            return;
        }
        HashMap<String, Object> params = new HashMap<>();
        params.put("vc", code);
        ParamUser user = new ParamUser();
        user.setLoginName(phoneNo.toString());


        NetUtils.executPost(mBaseActivity,Constants.CODE_RESET_PSW, "ResetPwd", params, ResultDetail.class);
        showLoading();


    }

    @Nullable
    private CharSequence checkPhone() {
        CharSequence phoneNo = et_phone.getText();
        if (TextUtils.isEmpty(phoneNo)) {
            Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();

            return null;
        } else if (!CommonUtils.isMobileNO(phoneNo.toString())) {
            Toast.makeText(this, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return null;
        }
        return phoneNo;
    }


    @Override
    public void callback(int code, BaseModel model) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        _handler.removeCallbacks(_timeCountRunable);
        resetCodeStatus();
    }

}

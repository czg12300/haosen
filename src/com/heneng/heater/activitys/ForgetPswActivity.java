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
import com.heneng.heater.utils.LogUtils;
import com.heneng.heater.utils.NetUtils;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/9/20.
 */
public class ForgetPswActivity extends BaseNetActivity {

    @InjectView(R.id.foget_psw_et_phone)
    EditText et_phone;
    @InjectView(R.id.foget_psw_et_code)
    EditText et_code;
    @InjectView(R.id.foget_psw_tv_get_code)
    TextView tv_get_code;
    @InjectView(R.id.foget_psw_tv_next)
    TextView tv_next;
    @InjectView(R.id.forget_psw_et_psw)
    EditText et_psw;
    @InjectView(R.id.forget_psw_et_confirm_psw)
    EditText et_confirm_psw;


    private int _timeCount = 61;

    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.a1_activity_forget_psw);
        ButterKnife.inject(this);
    }

    private boolean _isGetCode;

    @OnClick(R.id.foget_psw_tv_get_code)
    void getCode() {
        CharSequence phoneNo = checkPhone();
        if (phoneNo == null) return;

        HashMap<String, Object> params = new HashMap<>();
        params.put("phone", phoneNo);

        NetUtils.executGet(mBaseActivity,Constants.CODE_GET_CODE, "GetMsgValidateCode", params, ResultCommon.class);
        _isResetCodeStatus = false;
        _isGetCode = true;
        timeCount();

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

    void timeCount() {
        _timeCount -= 1;
        tv_get_code.setEnabled(false);
        if (_timeCount >= 0) {
            tv_get_code.setText(_timeCount + "秒");
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

    private boolean _isResetCodeStatus;

    private void resetCodeStatus() {
        tv_get_code.setText("获取验证码");
        _timeCount = 61;
        tv_get_code.setEnabled(true);
        _isResetCodeStatus = true;
        _isGetCode = false;
    }


    @OnClick(R.id.foget_psw_tv_next)
    void nextStep() {

        if(_isResetCodeStatus){
            Toast.makeText(mBaseActivity, "验证码已过期，需重新获取", Toast.LENGTH_SHORT).show();
            return;
        }


        CharSequence psw = et_psw.getText();
        CharSequence repsw = et_confirm_psw.getText();
        CharSequence code = et_code.getText();
        CharSequence phoneNo = checkPhone();
        if (phoneNo == null) {
            return;
        }

        if (!_isGetCode) {
            Toast.makeText(mBaseActivity, "请先获取验证码", Toast.LENGTH_SHORT).show();
            return;
        }


        if (TextUtils.isEmpty(code)) {
            Toast.makeText(mBaseActivity, "请输入短信验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        if(code.length()!=6){
            Toast.makeText(mBaseActivity, "请输入6位的验证码", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(psw)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        } else if (!TextUtils.equals(psw, repsw)) {
            Toast.makeText(mBaseActivity, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }


        HashMap<String, Object> params = new HashMap<>();
        params.put("vc", code);
        ParamUser user = new ParamUser();
        user.setLoginName(phoneNo.toString());
        user.setNewPwd(psw.toString());
        params.put("jsonInfo", CommonUtils.toJson(user));

        NetUtils.executPost(mBaseActivity,Constants.CODE_RESET_PSW, "ResetPwd", params, ResultDetail.class);
        showLoading();

    }


    @Override
    public void callback(int code, BaseModel model) {
        if (code == Constants.CODE_GET_CODE) {
            ResultCommon result = (ResultCommon) model;
            LogUtils.e("短信结果：" + result);
            if (Constants.RESULT_NO.equals(result.getResult())) {
                Toast.makeText(mBaseActivity, "获取验证码失败", Toast.LENGTH_SHORT).show();
                resetCodeStatus();
            }
        } else if (code == Constants.CODE_RESET_PSW) {
            closeLoading();
            ResultDetail result = (ResultDetail) model;
            Bundle extra = new Bundle();
            extra.putString(Constants.KEY_EXTRA_RESULT, result.getResult());
            extra.putString(Constants.KEY_EXTRA_INFO, result.getInfo());
            extra.putString(Constants.KEY_EXTRA_RESULT_TYPE,Constants.RESULT_TYPE_EDIT_PSW);
            openActivityWithParams(ShowResultActivity.class, extra);
            finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        _handler.removeCallbacks(_timeCountRunable);
    }
}

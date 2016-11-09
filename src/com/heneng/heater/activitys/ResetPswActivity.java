package com.heneng.heater.activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
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
 * Created by huangqland on 15/9/18.
 */
public class ResetPswActivity extends BaseNetActivity {

    @InjectView(R.id.repsw_et_psw)
    EditText et_psw;
    @InjectView(R.id.repsw_et_confirm_psw)
    EditText et_confirm_psw;
    @InjectView(R.id.repsw_tv_confirm)
    TextView tv_confirm;

    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.a1_activity_resetpsw);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.repsw_tv_confirm)
    public void onConfirm() {
        CharSequence psw = et_psw.getText();
        CharSequence repsw = et_confirm_psw.getText();
        if (TextUtils.isEmpty(psw)) {
            Toast.makeText(this, "请输入密码", Toast.LENGTH_SHORT).show();
            return;
        } else if (!TextUtils.equals(psw, repsw)) {
            Toast.makeText(mBaseActivity, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
            return;
        }


        HashMap<String, Object> params = new HashMap<>();
        params.put("uc", UserInfo.UserCode);
        ParamUser user = new ParamUser();
        user.setLoginName(UserInfo.Name);
        user.setNewPwd(psw.toString());

        params.put("jsonInfo", CommonUtils.toJson(user));

        NetUtils.executPost(mBaseActivity,Constants.CODE_RESET_PSW, "ResetPwd", params, ResultDetail.class);
        showLoading();
    }


    @Override
    public void callback(int code, BaseModel model) {
        if (code == Constants.CODE_RESET_PSW) {
            closeLoading();
            ResultDetail result = (ResultDetail) model;
            Bundle extra = new Bundle();
            extra.putString(Constants.KEY_EXTRA_RESULT, result.getResult());
            extra.putString(Constants.KEY_EXTRA_INFO, result.getInfo());
            openActivityWithParams(ShowResultActivity.class, extra);
            finish();
        }
    }
}

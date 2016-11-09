package com.heneng.heater.activitys;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.heneng.heater.R;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ParamSendDeviceInfo;
import com.heneng.heater.models.ResultDetail;
import com.heneng.heater.models.ResultDeviceInfo;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.NetUtils;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/12/24.
 */
public class EditDeviceInfoActivity extends BaseNetActivity {
    @InjectView(R.id.edit_device_et_name)
    EditText et_name;
    @InjectView(R.id.edit_device_et_dealer)
    EditText et_dealer;
    @InjectView(R.id.edit_device_et_province)
    EditText et_province;
    @InjectView(R.id.edit_device_et_city)
    EditText et_city;
    @InjectView(R.id.edit_device_et_town)
    EditText et_town;
    @InjectView(R.id.edit_device_et_address)
    EditText et_address;
    @InjectView(R.id.edit_device_et_owener)
    EditText et_owener;
    @InjectView(R.id.edit_device_et_phone)
    EditText et_phone;

    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.a1_activity_edit_device_info);
        ButterKnife.inject(this);

        initView();

    }

    private void initView() {
        et_name.setText(_app._chooseDevice.getEname());
        showLoading();
        Map<String, Object> paramsMap = getParamsMap();
        paramsMap.put("uc", UserInfo.UserCode);
        paramsMap.put("eid", _app._chooseDevice.getEID());
        NetUtils.executPost(mBaseActivity,Constants.CODE_GET_DEVICEINFO, "GetEquimentData", paramsMap, ResultDeviceInfo.class);
    }

    @Override
    public void callback(int code, BaseModel model) {

        if (code == Constants.CODE_GET_DEVICEINFO) {
            closeLoading();
            if (model == null) return;
            ResultDeviceInfo info = (ResultDeviceInfo) model;
            et_name.setText(info.getEname());
            et_dealer.setText(info.getAgent());
            et_province.setText(info.getPosition());
            et_city.setText(info.getPosition2());
            et_town.setText(info.getPosition3());
            et_address.setText(info.getAddress());
            et_owener.setText(info.getClientName());
            et_phone.setText(info.getClientPhone());
        }else  if(code == Constants.CODE_SET_DEVICEINFO){
            closeLoading();
            ResultDetail result = (ResultDetail) model;
            Bundle extra = new Bundle();
            extra.putString(Constants.KEY_EXTRA_RESULT, result.getResult());
            extra.putString(Constants.KEY_EXTRA_INFO, result.getInfo());
            extra.putString(Constants.KEY_EXTRA_RESULT_TYPE,Constants.RESULT_TYPE_OTHER);
            openActivityWithParams(ShowResultActivity.class, extra);
            finish();

        }
    }

    @OnClick(R.id.edit_device_tv_submit)
    public void submit() {
        CharSequence ename = et_name.getText();
        CharSequence owner = et_owener.getText();
        CharSequence phone = et_phone.getText();

        if (TextUtils.isEmpty(ename)) {
            Toast.makeText(mBaseActivity, "设备名不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        Map<String, Object> paramsMap = getParamsMap();
        paramsMap.put("uc", UserInfo.UserCode);
        Gson gson = new Gson();
        ParamSendDeviceInfo info = new ParamSendDeviceInfo();
        info.setEID(_app._chooseDevice.getEID());
        info.setClientName(owner.toString());
        info.setClientPhone(phone.toString());
        info.setEname(ename.toString());
        String jsoninfo = gson.toJson(info);
        paramsMap.put("jsonInfo", jsoninfo);
        NetUtils.executPost(mBaseActivity,Constants.CODE_SET_DEVICEINFO, "SetEquimentData", paramsMap, ResultDetail.class);
        showLoading();
    }


}

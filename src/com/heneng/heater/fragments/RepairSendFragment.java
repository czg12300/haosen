package com.heneng.heater.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ParamSendError;
import com.heneng.heater.models.ResultDetail;
import com.heneng.heater.models.ResultDeviceList;
import com.heneng.heater.models.ResultErrorList;
import com.heneng.heater.models.ResultGetNowData;
import com.heneng.heater.models.ResultReportFault;
import com.heneng.heater.models.ResultReportFaultList;
import com.heneng.heater.utils.CommonUtils;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.LogUtils;
import com.heneng.heater.utils.NetUtils;
import com.igexin.sdk.PushManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/9/20.
 */
public class RepairSendFragment extends BaseNetFragment {


    @InjectView(R.id.send_repair_sp_name)
    Spinner sp_name;
    @InjectView(R.id.send_repair_sp_code)
    Spinner sp_code;
    @InjectView(R.id.send_repair_et_address)
    EditText et_adress;
    @InjectView(R.id.send_repair_et_human)
    EditText et_human;
    @InjectView(R.id.send_repair_et_phone)
    EditText et_phone;
    @InjectView(R.id.send_repair_et_desc)
    EditText et_desc;


    List<String> _names;
    List<String> _errTexts;
    List<String> _errCodes;
    List<String> _eid;
    String _nowGetErrorCode;
    ArrayAdapter _nameAdapter;
    ArrayAdapter _errorAdapter;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View _view = inflater.inflate(R.layout.a1_fragment_repair_send, null);
        ButterKnife.inject(this, _view);

        initData();

        return _view;
    }

    public void initData() {
        initNameData();

        checkStatus();

        selectDefaultName();
    }


    @Override
    public void onResume() {
        super.onResume();
//        init();
        selectDefaultName();
    }

    private void selectDefaultName() {
        if (_app._chooseDevice != null) {
            int index = _names.indexOf(_app._chooseDevice.getEname());
            LogUtils.e("选中设备：" + _app._chooseDevice.getEname());
            if (index != -1) {
                sp_name.setSelection(index);
            }
        }else{

            sp_name.setSelection(0);
        }
    }


    private void initNameData() {
        //   LogUtils.e("初始化名字");
        _names = new ArrayList<>();
        _eid = new ArrayList<>();
        _names.add("请选择设备名称");
        for (int i = 0; i < _app._deviceList.size(); i++) {
            ResultDeviceList.Device item = _app._deviceList.get(i);
            _names.add(item.getEname());
            _eid.add(item.getEID());
        }

        _nameAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, _names);
        _nameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_name.setAdapter(_nameAdapter);


        sp_name.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                _app._chooseDevice = _app._deviceList.get(position - 1);
                checkStatus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        selectDefaultName();

    }

    private void checkStatus() {
        if(_app._chooseDevice == null)return;
        Map<String, Object> paramsMap = getParamsMap();
        paramsMap.put("uc", UserInfo.UserCode);
        paramsMap.put("eid", _app._chooseDevice.getEID());
        paramsMap.put("clientMac", PushManager.getInstance().getClientid(mContext));
//        LogUtils.e("UC:" + UserInfo.UserCode);
        NetUtils.executPost(mContext,Constants.CODE_GET_ERRORLIST, "Get_ErrorList", paramsMap, ResultErrorList.class);
        Map<String, Object> paramsMap1 = getParamsMap();
        paramsMap1.put("uc", UserInfo.UserCode);
        paramsMap1.put("user", UserInfo.ID);
        paramsMap1.put("eid", _app._chooseDevice.getEID());

        NetUtils.executPost(mContext,Constants.CODE_GET_GETREPORTFAULT, "GetReportFault", paramsMap1, ResultReportFault.class);
        NetUtils.executGet(mContext,Constants.CODE_GET_GETNOWDATA, _app._chooseDevice.getInterface(), "GetNowData", paramsMap, ResultGetNowData.class);
        showLoading();
    }





    private void initErrorData(ResultErrorList errorList) {

        _errCodes = new ArrayList<>();
        _errTexts = new ArrayList<>();
        _errTexts.add("请选择故障");
        for (int i = 0; i < errorList.getData().size(); i++) {
            ResultErrorList.DataEntity item = errorList.getData().get(i);
            _errCodes.add(item.getErrCode());
            _errTexts.add(item.getErrtext() + "(" + item.getErrShotText() + ")");
            //   LogUtils.e("初始化错误代码:" + item.getErrtext() + "(" + item.getErrShotText() + ")");
        }

        _errorAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, _errTexts);
        _errorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_code.setAdapter(_errorAdapter);


        sp_code.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return;
                }
                sp_code.setTag(_errCodes.get(position - 1));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (_nowGetErrorCode != null) {
            int index = _errCodes.indexOf(_nowGetErrorCode);
            sp_code.setSelection(index + 1);
        } else {

            sp_code.setSelection(0);
        }
    }


    @Nullable
    private CharSequence checkPhone() {
        CharSequence phoneNo = et_phone.getText();
        if (TextUtils.isEmpty(phoneNo)) {
            Toast.makeText(mContext, "请输入手机号", Toast.LENGTH_SHORT).show();

            return null;
        } else if (!CommonUtils.isMobileNO(phoneNo.toString())) {
            Toast.makeText(mContext, "请输入正确的手机号", Toast.LENGTH_SHORT).show();
            return null;
        }
        return phoneNo;
    }

    @OnClick(R.id.send_repair_tv_commit)
    void onClickSend() {
        String name = sp_name.getSelectedItem().toString();
        CharSequence address = et_adress.getText();
        CharSequence human = et_human.getText();
        CharSequence phone = et_phone.getText();
        CharSequence desc = et_desc.getText();

        if (TextUtils.isEmpty(name)) {
            Toast.makeText(mContext, "请先选设备", Toast.LENGTH_SHORT).show();
            return;
        }
        sp_code.setTag("19");

        if (sp_code.getTag() == null) {
            Toast.makeText(mContext, "请先选故障", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(address)) {
            Toast.makeText(mContext, "请填写您的地址", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(human)) {
            Toast.makeText(mContext, "请填写您的称呼", Toast.LENGTH_SHORT).show();
            return;
        }
        if (checkPhone() == null) {
            return;
        }
//        String eid = _eid.get(_names.indexOf(name.toString()));
        Map<String, Object> params = getParamsMap();
        ParamSendError error = new ParamSendError();
        error.setAddress(address.toString());
        error.setEid(_app._chooseDevice.getEID());
        error.setClientID(UserInfo.ID + "");
        error.setClientName(human.toString());
        error.setErrorCode(sp_code.getTag().toString());
        error.setErrorDescription(desc.toString());
        error.setClientPhone(phone.toString());
        params.put("uc", UserInfo.UserCode);
        params.put("jsonInfo", CommonUtils.toJson(error));

        NetUtils.executPost(mContext,Constants.CODE_GET_SEND_ERROR, "AddReportFault", params, ResultDetail.class);
        showLoading();


    }


    @Override
    public void callback(int code, BaseModel model) {

        if (code == Constants.CODE_GET_SEND_ERROR && model != null) {
            ResultDetail result = (ResultDetail) model;
            closeLoading();
            if (Constants.RESULT_YES.equals(result.getResult())) {
                Toast.makeText(mContext, "提交故障单成功", Toast.LENGTH_SHORT).show();
                resetData();
                Map<String, Object> paramsMap = getParamsMap();
                paramsMap.put("uc", UserInfo.UserCode);
                paramsMap.put("user", UserInfo.ID);
                paramsMap.put("eid", _app._chooseDevice.getEID());
                NetUtils.executPost(mContext,Constants.CODE_GET_GETREPORTFAULT, "GetReportFault", paramsMap, ResultReportFault.class);

//                ResultReportFault.DataEntity entity = new ResultReportFault.DataEntity();
//                entity.setStep("2");
//                Intent intent = new Intent();
//                intent.setAction(Constants.BROCAST_JUMP_TO_REPAIRPROGRESS);
//                intent.putExtra(Constants.KEY_EXTRA_RESULTREPORTFAULT, entity);
//                mContext.sendBroadcast(intent);


            } else {
                Toast.makeText(mContext, "提交故障单失败", Toast.LENGTH_SHORT).show();
            }


        }
//        else if (code == Constants.CODE_GET_GETREPORTFAULT && model != null) {
//            ResultReportFault report = (ResultReportFault) model;
//            if (report.getData() != null) {
//
//                for (int i = 0; i < report.getData().size(); i++) {
//                    ResultReportFault.DataEntity entity = report.getData().get(i);
//                    LogUtils.e("FaultList："+entity.getEName());
//                    if(entity.getEName()!=null&&entity.getEName().equals(_app._chooseDevice.getEname())){
//                        Map<String, Object> paramsMap = getParamsMap();
//                        paramsMap.put("uc", UserInfo.UserCode);
//                        paramsMap.put("user", UserInfo.ID);
//                        paramsMap.put("rid",entity.getID());
//                        NetUtils.executPost(Constants.CODE_GET_GETREPORTFAULT, "GetReportFault", paramsMap, ResultReportFault.class);
//                        break;
//                    }
//                }
//
//            }
//        }
        else if (code == Constants.CODE_GET_GETREPORTFAULT) {
            ResultReportFault result = (ResultReportFault) model;
            if (result == null || result.getData().size() == 0) {
                Log.e("TAG","这个设备没有维修记录");
                return;
            }

            ResultReportFault.DataEntity entity = result.getData().get(0);

            if ("3".equals(entity.getStep())) {
                Intent intent = new Intent();
                intent.setAction(Constants.BROCAST_JUMP_TO_REPAIREVALUATION);
                intent.putExtra(Constants.KEY_EXTRA_RESULTREPORTFAULT, entity);
                mContext.sendBroadcast(intent);

            } else {
                Intent intent = new Intent();
                intent.setAction(Constants.BROCAST_JUMP_TO_REPAIRPROGRESS);
                intent.putExtra(Constants.KEY_EXTRA_RESULTREPORTFAULT, entity);
                mContext.sendBroadcast(intent);
            }
        } else if (code == Constants.CODE_GET_ERRORLIST) {
            closeLoading();
            if (model == null) {
                //   Toast.makeText(mContext, "这个设备查询不到故障记录", Toast.LENGTH_SHORT).show();
                return;
            }
            ResultErrorList errorList = (ResultErrorList) model;
            initErrorData(errorList);

        } else if (code == Constants.CODE_GET_GETNOWDATA) {
            ResultGetNowData data = (ResultGetNowData) model;
            String error = data.getError();
            _nowGetErrorCode = error;
            if (_errCodes != null) {
                int index = _errCodes.indexOf(error);
                LogUtils.e("设备错误码：" + error + "---" + index);
                sp_code.setSelection(index + 1);
            }
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void resetData() {
        sp_name.setSelection(0);
        sp_code.setSelection(0);
        et_adress.setText("");
        et_desc.setText("");
        et_human.setText("");
        et_phone.setText("");
    }
}

package com.heneng.heater.activitys;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.models.BaseModel;
import com.heneng.heater.models.ResultUpdate;
import com.heneng.heater.utils.CommonUtils;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.LogUtils;
import com.heneng.heater.utils.NetUtils;
import com.heneng.heater.views.HeaderView;
import com.heneng.heater.views.SettingItem;

import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by huangqland on 15/9/18.
 */
public class UpdateActivity extends BaseNetActivity {

    @InjectView(R.id.update_item_check)
    SettingItem item_check;

    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.a1_activity_update);
        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        if (_sp.getBoolean(Constants.KEY_SP_CAN_UPDATE, false)) {
            LogUtils.e("有新版本");
            item_check.setSubTitle("有新版本");
        }else{
            item_check.setSubTitle("已是最新版本");
        }
        item_check.setTitle("昊森热能(" + CommonUtils.getLocalVersionName(mBaseActivity) + ")");
    }


    @OnClick(R.id.update_item_check)
    void checkUpdate() {
        Map<String, Object> params = getParamsMap();
        params.put("uc", UserInfo.UserCode);
        params.put("type", "1");
        NetUtils.executGet(mBaseActivity,Constants.CODE_GET_UPDATE, "GetVersionInfo", params, ResultUpdate.class);
        showLoading();
    }


    @Override
    public void callback(int code, BaseModel model) {
        if (Constants.CODE_GET_UPDATE == code) {
            closeLoading();
            final ResultUpdate result = (ResultUpdate) model;
            int localVersion = CommonUtils.getLocalVersionCode(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            if (localVersion <Integer.parseInt(result.getVersion())) {
                //有升级
                item_check.setSubTitle("有新版本");
                builder.setTitle("有新版本");
                builder.setMessage("版本：" + result.getName() + "\n描述：" + Html.fromHtml( result.getDescription()).toString());
                builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        downLoad(result.getAppPath(),result.getVersion(),false);
                    }
                });
                builder.setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });

            } else {
                //无升级
                builder.setTitle("提示");
                builder.setMessage("已是最新版本");
                item_check.setSubTitle("已是最新版本");
                builder.setNeutralButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
            }
            builder.setCancelable(false);
            builder.create().show();
        }
    }

    private void downLoad(String appPath,String version,boolean isForce) {
        NetUtils.startDownLoad(this, appPath,version,isForce);
    }


}

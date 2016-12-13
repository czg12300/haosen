package com.heneng.heater.activitys;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.heneng.heater.R;
import com.heneng.heater.esptouch.demo_activity.EspWifiAdminSimple;
import com.heneng.heater.utils.Constants;
import com.heneng.heater.utils.ReceiverActions;
import com.heneng.heater.views.HeaderView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/10/9.
 */
public class SmartConfigWifiActivity extends BaseActivity {
    @InjectView(R.id.smartconfig_wifi_et_wifi)
    EditText et_ssid;
    @InjectView(R.id.smartconfig_wifi_et_psw)
    EditText et_psw;


    private EspWifiAdminSimple mWifiAdmin;
    private BroadcastReceiver mReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (TextUtils.equals(ReceiverActions.ACTION_FINISH_SMART_CONFIG, intent.getAction())) {
                    finish();
                }
            }
        };
        IntentFilter filter = new IntentFilter();
        filter.addAction(ReceiverActions.ACTION_FINISH_SMART_CONFIG);
        registerReceiver(mReceiver, filter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.a1_activity_smartconfig_wifi);
        ButterKnife.inject(this);
        mWifiAdmin = new EspWifiAdminSimple(this);
        header.setRightListener(new HeaderView.onClickRightListener() {
            @Override
            public void onRightClick(View view) {
                toConnect();
            }
        });
        noEnterKeyEvent(et_psw);
        noEnterKeyEvent(et_ssid);
    }

    private void noEnterKeyEvent(EditText et) {
        et.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                return (event.getKeyCode() == KeyEvent.KEYCODE_ENTER);
            }
        });
    }


    void toConnect() {
        final CharSequence ssid = et_ssid.getText();
        final CharSequence psw = et_psw.getText();
        if (TextUtils.isEmpty(ssid)) {
            Toast.makeText(mBaseActivity, "SSID不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(psw)) {
            //  Toast.makeText(mBaseActivity, "密码不能为空", Toast.LENGTH_SHORT).show();
            AlertDialog.Builder builder = new AlertDialog.Builder(mBaseActivity);
            builder.setMessage("您输入的密码为空，是否继续操作").setTitle("提示").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Bundle extra = new Bundle();
                    extra.putString(Constants.KEY_EXTRA_SSID, ssid.toString());
                    extra.putString(Constants.KEY_EXTRA_WIFI_PSW, psw.toString());
                    openActivityWithParams(SmartConfigActivity.class, extra);
                }
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            }).create();
            builder.show();
            return;
        }

        Bundle extra = new Bundle();
        extra.putString(Constants.KEY_EXTRA_SSID, ssid.toString());
        extra.putString(Constants.KEY_EXTRA_WIFI_PSW, psw.toString());
        openActivityWithParams(SmartConfigActivity.class, extra);
    }

    @Override
    protected void onResume() {
        super.onResume();
        String apSsid = mWifiAdmin.getWifiConnectedSsid();
        if (apSsid != null) {
            et_ssid.setText(apSsid);
        } else {
            et_ssid.setText("");
        }

    }
}

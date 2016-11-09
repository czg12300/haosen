package com.heneng.heater.lastcoder;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.LoginFilter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.heneng.heater.R;
import com.heneng.heater.activitys.ForgetPswActivity;
import com.heneng.heater.lastcoder.common.CheckFastClick;
import com.heneng.heater.lastcoder.common.CommonFunction;
import com.heneng.heater.lastcoder.common.GetRemoteData;
import com.heneng.heater.lastcoder.model.AppParams;
import com.heneng.heater.lastcoder.model.MonitorInfo;
import com.heneng.heater.lastcoder.model.PostResult;
import com.heneng.heater.lastcoder.model.UserInfo;
import com.heneng.heater.services.DownloadService;
import com.igexin.sdk.PushManager;


import org.json.JSONObject;

public class LoginActivity extends FragmentActivity {

    EditText username;
    EditText userpwd;
    String usernameVal;
    String userpwdVal;
    String userBarcode; //用户最后控制的设备条码
    boolean userBarcodeExist = false;//用户与记录的userBarcode是否存在绑定关系
    CheckBox autologin;
    SharedPreferences sharedPreferences;

    Boolean isLogining = false;

    @Override
    protected void onResume() {
        super.onResume();
        resetForm();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) this.findViewById(R.id.login_username);
        userpwd = (EditText) this.findViewById(R.id.login_password);
        autologin = (CheckBox) this.findViewById(R.id.login_autoLogin);

        findViewById(R.id.login_tv_forget_psw).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this, ForgetPswActivity.class);
                startActivity(intent);
            }
        });


        sharedPreferences = getSharedPreferences("uinfo", Context.MODE_PRIVATE);


        Button button = (Button) this.findViewById(R.id.login_submit);
        button.setOnClickListener(new View.OnClickListener() {
            //����ð�ť���һ���µ�Activity
            @SuppressWarnings("deprecation")
            public void onClick(View v) {

                if (!CheckFastClick.IsFastClick() && !isLogining) {

                    Editor editor = sharedPreferences.edit();
                    usernameVal = username.getText().toString().trim();
                    userpwdVal = userpwd.getText().toString().trim();
                    if (usernameVal.equals("")) {
                        Toast.makeText(LoginActivity.this, "请填写用户名", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (autologin.isChecked()) {
                        editor.putString("username", usernameVal);
                        editor.putString("userpwd", userpwdVal);
                        editor.putString("autologin", "1");
                    } else {
                        editor.putString("username", "");
                        editor.putString("userpwd", "");
                        editor.putString("autologin", "0");
                    }
                    editor.commit();

                    AsyncLoginTask asy = new AsyncLoginTask();
                    asy.execute();
                }
            }
        });

        Button registerBtn = (Button) this.findViewById(R.id.login_register);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                if (!CheckFastClick.IsFastClick() && !isLogining) {
                    Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                    startActivity(intent);
                }
            }
        });


        WindowManager wm = LoginActivity.this.getWindowManager();
        int width = wm.getDefaultDisplay().getWidth();
        int height = wm.getDefaultDisplay().getHeight();
        MonitorInfo.Width = CommonFunction.px2dip(LoginActivity.this, width);
        MonitorInfo.Height = CommonFunction.px2dip(LoginActivity.this, height);
        MonitorInfo.WidthRate = 1;
        if (MonitorInfo.Width <= 320)
            MonitorInfo.WidthRate = (double) 320 / 360;
        else if (MonitorInfo.Width >= 480)
            MonitorInfo.WidthRate = (double) 480 / 360;

        Log.d("monitorInfo", "width:" + MonitorInfo.Width + "/height:" + MonitorInfo.Height);


        Intent serviceIntent = new Intent();
        serviceIntent.setClass(this, DownloadService.class);
        startService(serviceIntent);


        PushManager.getInstance().initialize(this.getApplicationContext());


    }

    private void resetForm() {
        String _al = sharedPreferences.getString("autologin", "");
        String _un = sharedPreferences.getString("username", "");
        String _up = sharedPreferences.getString("userpwd", "");
        userBarcode = sharedPreferences.getString("userbarcode", "");
        if (_al.equals("1")) autologin.setChecked(true);
        if (_un != null) username.setText(_un);
        if (_up != null) userpwd.setText(_up);
    }

    class AsyncLoginTask extends AsyncTask<Void, Void, PostResult> {

        public AsyncLoginTask() {
            super();
        }

        @Override
        protected void onPreExecute() {
            if (isLogining == true)
                this.cancel(true);
            else
                ShowLoading();
            isLogining = true;
        }

        @Override
        protected PostResult doInBackground(Void... params) {
            PostResult result = new PostResult();
            try {
                String strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=SystemLogin&u="
                        + usernameVal + "&p=" + userpwdVal + "&clientMac=" + PushManager.getInstance().getClientid(LoginActivity.this);
                //Log.d("loginPost", "loginPost:"+strUrl);
                String jstr = GetRemoteData.ConnServerForResult(strUrl, LoginActivity.this);
                if (!jstr.equals("")) {
                    JSONObject jsObj = GetRemoteData.ParseJson(jstr, LoginActivity.this);
                    String _r = jsObj.getString("Result");
                    String _info = jsObj.getString("Info");
                    String _uc = jsObj.getString("Code");
                    String _uid = jsObj.getString("UserID");

                    if (_r.equals("1")) {
                        result.Result = "1";
                        UserInfo.ID = Integer.parseInt(_uid);
                        UserInfo.Name = usernameVal;
                        UserInfo.UserCode = _uc;

                        if (!userBarcode.equals("")) {
                            String strUrl2 = AppParams.WebApi + "/AppHandler.ashx?Method=CheckUserEquiment&uc="
                                    + UserInfo.UserCode + "&user=" + UserInfo.ID + "&eid=" + userBarcode;
                            String jstr2 = GetRemoteData.ConnServerForResult(strUrl2, LoginActivity.this);
                            if (!jstr2.equals("")) {
                                JSONObject jsObj2 = GetRemoteData.ParseJson(jstr2, LoginActivity.this);
                                String _r2 = jsObj2.getString("Result");
                                //String _info2 = jsObj2.getString("Info");

                                if (_r2.equals("1")) {
                                    userBarcodeExist = true;
                                } else {
                                    userBarcodeExist = false;
                                }
                            }
                        }
                    } else {
                        result.Result = "0";
                        result.Info = _info;
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
                result.Result = "0";
                result.Info = "网络不通，请检查网络设置";

            } finally {
                return result;
            }
        }


        @Override
        protected void onPostExecute(PostResult result) {
            super.onPostExecute(result);

            if (result.Result.equals("0")) {
                Toast.makeText(LoginActivity.this, result.Info, Toast.LENGTH_SHORT).show();
                CloseLoading();
                isLogining = false;
                return;
            }

            Intent intent;
//            if (userBarcodeExist) {
//                intent = new Intent(LoginActivity.this, DetailActivity.class);
//                Bundle bundle = new Bundle();
//                bundle.putString("eid", userBarcode);
//                bundle.putString("refererPage", "login");
//                intent.putExtras(bundle);
//            } else {
            intent = new Intent(LoginActivity.this, MainActivity.class);
//            }

            ((AppAplication) getApplication()).handleLoginHeart();
            startActivity(intent);
            CloseLoading();
            isLogining = false;
            finish();
        }
    }

    LoadingFragment loadingDialog = null;

    public void ShowLoading() {
        if (loadingDialog == null) {
            loadingDialog = new LoadingFragment();
            loadingDialog.setCancelable(false);
            loadingDialog.show(getSupportFragmentManager(), "Loading");
        }
    }

    public void CloseLoading() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

}

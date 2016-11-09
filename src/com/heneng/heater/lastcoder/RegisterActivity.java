package com.heneng.heater.lastcoder;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.heneng.heater.R;
import com.heneng.heater.lastcoder.common.GetRemoteData;
import com.heneng.heater.lastcoder.model.AppParams;
import com.heneng.heater.lastcoder.model.PostResult;
import com.heneng.heater.views.HeaderView;


import java.net.URL;
import java.net.URLEncoder;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends FragmentActivity {

    private EditText register_phone_field;
    private EditText register_authcode_field;
    private Button register_authcode_getbutton;
    private EditText register_password_field;
    private CheckBox register_agreement_check;
    private Button register_submit;
    private CountDownTimer timer;
    private Executor executor;
    public HeaderView header;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        executor = Executors.newSingleThreadExecutor();

        header = (HeaderView)findViewById(R.id.header);
        header.setLeftListener(new HeaderView.onClickLeftListener() {
            @Override
            public void onLeftClick(View view) {
                RegisterActivity.this.finish();
            }
        });

        register_phone_field = (EditText) findViewById(R.id.register_phone_field);
        register_authcode_field = (EditText) findViewById(R.id.register_authcode_field);
        register_authcode_getbutton = (Button) findViewById(R.id.register_authcode_getbutton);
        register_password_field = (EditText) findViewById(R.id.register_password_field);
        register_agreement_check = (CheckBox) findViewById(R.id.register_agreement_check);
        register_submit = (Button) findViewById(R.id.register_submit);

        register_authcode_getbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = register_phone_field.getText().toString();
                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getBaseContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isMobileNO(phone)) {
                    Toast.makeText(getBaseContext(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }

                GetValidateCodeTask task = new GetValidateCodeTask();
                task.executeOnExecutor(executor, phone);

                final long totalTime = 60 * 1000;
                timer = new CountDownTimer(totalTime, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        register_authcode_getbutton.setText("倒计时" + millisUntilFinished / 1000 + "s");
                        register_authcode_getbutton.setEnabled(false);
                    }

                    @Override
                    public void onFinish() {
                        register_authcode_getbutton.setText("获取验证码");
                        register_authcode_getbutton.setEnabled(true);
                    }
                };
                timer.start();
            }
        });

        register_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phone = register_phone_field.getText().toString().trim();
                String pwd = register_password_field.getText().toString();
                String code = register_authcode_field.getText().toString().trim();

                if (TextUtils.isEmpty(phone)) {
                    Toast.makeText(getBaseContext(), "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isMobileNO(phone)) {
                    Toast.makeText(getBaseContext(), "请输入正确的手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(code)) {
                    Toast.makeText(getBaseContext(), "请输入验证码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(pwd)) {
                    Toast.makeText(getBaseContext(), "请输入登陆密码", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!register_agreement_check.isChecked()) {
                    Toast.makeText(getBaseContext(), "请阅读条款，并确认同意", Toast.LENGTH_SHORT).show();
                    return;
                }

                LoginInfo loginInfo = new LoginInfo();
                loginInfo.LoginName = phone;
                loginInfo.Pwd = pwd;

                GetRegisterTask task = new GetRegisterTask();
                task.executeOnExecutor(executor, code, new Gson().toJson(loginInfo));
                register_submit.setEnabled(false);
            }
        });

    }

    //判断手机格式是否正确
    public boolean isMobileNO(String mobiles) {
        Pattern p = Pattern.compile("^((13[0-9])|(15[0-9])|(18[0-9]))\\d{8}$");//"^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$"
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    class GetValidateCodeTask extends AsyncTask<String, Void, PostResult> {

        @Override
        protected PostResult doInBackground(String... params) {
            try {
                String phone = params[0];
                String strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=GetMsgValidateCode&phone=" + phone;
                String jstr = GetRemoteData.ConnServerForResult(strUrl, getBaseContext());
                if (!TextUtils.isEmpty(jstr)) {
                    return new Gson().fromJson(jstr, PostResult.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(PostResult result) {
            super.onPostExecute(result);
            if (result != null) {
                Toast.makeText(getBaseContext(), result.Info, Toast.LENGTH_SHORT).show();
            }
        }
    }

    class GetRegisterTask extends AsyncTask<String, Void, PostResult> {

        @Override
        protected PostResult doInBackground(String... params) {
            try {
                String vc = params[0];
                String userinfo = URLEncoder.encode(params[1], "UTF-8");
                String strUrl = AppParams.WebApi + "/AppHandler.ashx?Method=UserRegistration&vc=" + vc + "&jsonInfo=" + userinfo;
                String jstr = GetRemoteData.ConnServerForResult(strUrl, getBaseContext());
                if (!TextUtils.isEmpty(jstr)) {
                    return new Gson().fromJson(jstr, PostResult.class);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(PostResult result) {
            super.onPostExecute(result);
            register_submit.setEnabled(true);
            if (result != null) {
                Intent intent = new Intent(getBaseContext(), RegisterResultActivity.class);
                intent.putExtra("data", result);
                startActivity(intent);
            }
        }
    }

    public class LoginInfo {
        public String LoginName;
        public String Pwd;
    }
}

package com.heneng.heater.lastcoder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.heneng.heater.R;
import com.heneng.heater.lastcoder.model.PostResult;


/**
 * Created by KUANGJH on 2015-8-22.
 */
public class RegisterResultActivity extends FragmentActivity {

    private LinearLayout success_layout;
    private LinearLayout fail_layout;
    private TextView register_reason;
    private Button register_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist_result);

        if (!getIntent().hasExtra("data")) {
            finish();
            return;
        }

        GeneralTop mTop = (GeneralTop) getSupportFragmentManager().findFragmentById(R.id.register_title);
        if (mTop != null) {
            mTop.BindData("用户注册", "返回");
        }

        success_layout = (LinearLayout) findViewById(R.id.success_layout);
        fail_layout = (LinearLayout) findViewById(R.id.fail_layout);
        register_reason = (TextView) findViewById(R.id.register_reason);
        register_home = (Button) findViewById(R.id.register_home);

        register_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });

        PostResult result = (PostResult) getIntent().getSerializableExtra("data");
        if (result.Result.equals("1")) {
            success_layout.setVisibility(View.VISIBLE);
            fail_layout.setVisibility(View.GONE);
        } else {
            success_layout.setVisibility(View.GONE);
            fail_layout.setVisibility(View.VISIBLE);
            register_reason.setText("原因：" + result.Info);
        }
    }
}

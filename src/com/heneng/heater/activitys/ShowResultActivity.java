package com.heneng.heater.activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.heneng.heater.R;
import com.heneng.heater.utils.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/12/21.
 */
public class ShowResultActivity extends BaseActivity {
    @InjectView(R.id.repsw_result_im)
    ImageView im_result;
    @InjectView(R.id.repsw_result_tv)
    TextView tv_result;

    @InjectView(R.id.repsw_result_tv_info)
    TextView tv_info;

    private String _type;

    @Override
    public void init(Bundle savedInstanceState) {
        setContentView(R.layout.a1_activity_resetpsw_result);
        ButterKnife.inject(this);
        Intent intent = getIntent();
        String result = intent.getStringExtra(Constants.KEY_EXTRA_RESULT);
        String info = intent.getStringExtra(Constants.KEY_EXTRA_INFO);
        String type = intent.getStringExtra(Constants.KEY_EXTRA_RESULT_TYPE);
        _type = type;

        if (Constants.RESULT_NO.equals(result)) {
            im_result.setImageResource(R.drawable.result_no);
            tv_info.setText("原因：" + info);
            tv_result.setText("操作失败");
            header.setTitle("操作失败");
        } else {
            if (Constants.RESULT_TYPE_EDIT_PSW.equals(type)) {
                SharedPreferences.Editor editor = _sp.edit();
                editor.putString("userpwd", "");
                editor.putString("autologin", "0");
                editor.commit();
            }else{
                tv_result.setText("恭喜,操作成功");
            }
        }

    }


    @OnClick(R.id.repsw_result_tv_gohome)
    void goHome() {

        if(Constants.RESULT_TYPE_EVALUATION.equals(_type)){
            //跳转到提交页面
            Intent intent = new Intent();
            intent.setAction(Constants.BROCAST_JUMP_TO_REPAIRSEND);
            mBaseActivity.sendBroadcast(intent);
        }

        ShowResultActivity.this.finish();

    }
}
